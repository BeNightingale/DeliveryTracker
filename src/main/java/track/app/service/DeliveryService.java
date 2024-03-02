package track.app.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import track.app.InPostJsonDeserializer;
import track.app.PolishPostJsonDeserializer;
import track.app.mapper.Mapper;
import track.app.model.Deliverer;
import track.app.model.Delivery;
import track.app.model.DeliveryStatus;
import track.app.model.History;
import track.app.model.dto.DeliveryDto;
import track.app.model.dto.HistoryDto;
import track.app.model.dto.StatusChange;
import track.app.repository.DeliveryRepository;
import track.app.repository.HistoryRepository;

import java.time.LocalDateTime;
import java.util.*;

import static track.app.model.Deliverer.INPOST;
import static track.app.model.Deliverer.POCZTA_POLSKA;

@Service
@AllArgsConstructor
@Getter
@Slf4j
public class DeliveryService {

    private final Gson inPostGson = initGson(new InPostJsonDeserializer());
    private final Gson polishPostGson = initGson(new PolishPostJsonDeserializer());
    public final Map<Deliverer, Gson> gsonsMap = Map.of(
            INPOST, inPostGson,
            POCZTA_POLSKA, polishPostGson
    );

    private DeliveryRepository deliveryRepository;
    private HistoryRepository historyRepository;

    public int updateActiveDeliveriesStatuses(List<DeliveryDto> deliveryDtoList) {
        log.info("Init: deliveryDtoList = {}.", deliveryDtoList);
        if (CollectionUtils.isEmpty(deliveryDtoList)) {
            log.debug("Nothing to update. List provided by api deliverer is empty.");
            return 0;
        }
        int counter = 0;
        for (DeliveryDto dto : deliveryDtoList) {
            counter += updateDeliveryStatus(dto);
        }
//  Tylko do testów:
//        log.error("THis!!!!");
//        throw new RuntimeException("Unexpected error!");
        return counter;
    }

    protected int updateDeliveryStatus(DeliveryDto deliveryDto) {
        if (deliveryDto == null) {
            log.debug("DeliveryDto is null.");
            return 0;
        }
        final Deliverer deliverer = deliveryDto.getDeliverer();
        final String deliveryNumber = deliveryDto.getDeliveryNumber();
        final Delivery delivery = deliveryRepository.findDeliveryByDeliveryNumberAndDeliverer(deliveryNumber, deliverer);
        log.debug("[Before update] State delivery in database: {}.", delivery);
        if (delivery == null) {
            return 0;
        }
        log.info("[Before update] Found delivery in database with deliveryNumber = {} and status {}.", delivery.getDeliveryNumber(), delivery.getDeliveryStatus());
        // Mapowanie na status api DeliveryTracker.
        final DeliveryStatus deliveryDtoStatus = Mapper.statusMapperFunctions
                .get(deliverer)
                .apply(deliveryDto.getDeliveryStatus());
        log.debug("[Before update] Delivery in {} api with deliveryNumber = {} has current status {}.",
                deliverer, delivery.getDeliveryNumber(), delivery.getDeliveryStatus());
        if (deliveryDtoStatus == delivery.getDeliveryStatus()) {
            log.debug("[Before update] Delivery status hasn't changed. No status update will be made.");
            // status się nie zmienił, więc nie ma po co sprawdzać historii w takim przypadku - powinna byc kompletna
            // przy pierwszym zaciąganiu danych w bazie jest UNKNOWN a deliverer ma inny
            // TODO (ew nie znalazł, co wtedy?)
            // jeśli ma inny, to jest zmiana statusu i nie jestem w tym scopie
            return 0;
        }
        final int deliveryId = delivery.getDeliveryId();
        final LocalDateTime statusChangeDatetime = deliveryDto.getStatusChangeDateTime();
        final String statusDescription = deliveryDto.getStatusDescription();
        delivery.setDeliveryStatus(deliveryDtoStatus);
        delivery.setStatusDescription(statusDescription);
        delivery.setStatusChangeDatetime(statusChangeDatetime);
        delivery.setFinished(deliveryDto.getFinished());
        final Delivery afterUpdate = deliveryRepository.save(delivery);
        log.debug("[After update] New delivery state expected after update in database: {}.", afterUpdate);
        // After status update in 'delivery' table there is to be written entry in table 'history'.
        History history = new History();
        history.setDeliveryId(deliveryId);
        history.setDeliveryNumber(deliveryNumber);
        history.setDeliveryStatus(deliveryDtoStatus);
        history.setStatusDescription(statusDescription);
        history.setStatusChangeDatetime(statusChangeDatetime);
        final History historySaved = historyRepository.save(history);
        //    List<History> historyList = historyRepository.findHistoryByDeliveryId(deliveryId);
        log.debug("[After saving history event] New history saved: {}.", historySaved);
        final int historyRowsChangedNumber = updateDeliveryHistory(deliveryDto);
        log.error("The number of rows changed in history table: {}", historyRowsChangedNumber + 1);
        return 1;
    }

    public int updateDeliveryHistory(DeliveryDto deliveryDto) {// od delivera
        final Deliverer deliverer = deliveryDto.getDeliverer();
        final List<StatusChange> statusChangeList = deliveryDto.getStatusChangesList();// statusy nie zmapowane
        if (CollectionUtils.isEmpty(statusChangeList)) {
            log.debug("The list of statuses changes brought from deliverer is empty. Nothing to save in history table.");
            return 0;
        }
        final List<History> historyList = historyRepository.findHistoryByDeliveryId(deliveryDto.getDeliveryId());
        if (CollectionUtils.isEmpty(historyList)) {// ten przypadek raczej nie może zaistnieć, bo przy pierwszym wpisie do bazy, od razu idzie wpis do historii ze statusem UNKNOWN
            log.debug("The history list for the delivery with deliveryNumber = {} in database history table is empty.", deliveryDto.getDeliveryNumber());
            return saveAllStatusesChanges(statusChangeList, deliveryDto);
        }
        final Map<LocalDateTime, String> statusChangesMap = historyListToStatusesMap(historyList);
        int changedRowsCounter = 0;
        for (StatusChange statusChange : statusChangeList) { // idziemy po liście statusów od delivera
            final String statusFromDelivererList = statusChange.getStatus();
            final LocalDateTime statusChangeDateTime = statusChange.getStatusChangeTimeStamp();
            final String statusDescriptionInHistory = statusChangesMap.get(statusChangeDateTime);
            if (!Objects.equals(statusDescriptionInHistory, statusFromDelivererList)) {
                final DeliveryStatus deliveryDtoStatus = Mapper.statusMapperFunctions
                        .get(deliverer)
                        .apply(statusFromDelivererList);
                final HistoryDto history = HistoryDto
                        .builder()
                        .deliveryId(deliveryDto.getDeliveryId())
                        .deliveryNumber(deliveryDto.getDeliveryNumber())
                        .deliveryStatus(deliveryDtoStatus)
                        .statusDescription(statusFromDelivererList)
                        .statusChangeDateTime(statusChangeDateTime)
                        .build();
                historyRepository.save(history.toHistory());
                changedRowsCounter++;
            }
        }
        return changedRowsCounter;
    }

    private int saveAllStatusesChanges(List<StatusChange> statusChangeList, DeliveryDto deliveryDto) {
        final Deliverer deliverer = deliveryDto.getDeliverer();
        int changedRows = 0;
        for (StatusChange statusChange : statusChangeList) {
            final DeliveryStatus deliveryDtoStatus = Mapper.statusMapperFunctions
                    .get(deliverer)
                    .apply(statusChange.getStatus());
            final HistoryDto history = HistoryDto
                    .builder()
                    .deliveryId(deliveryDto.getDeliveryId())
                    .deliveryNumber(deliveryDto.getDeliveryNumber())
                    .deliveryStatus(deliveryDtoStatus)
                    .statusDescription(statusChange.getStatus())
                    .statusChangeDateTime(statusChange.getStatusChangeTimeStamp())
                    .build();
            historyRepository.save(history.toHistory());
            changedRows++;
        }
        return changedRows;
    }

    private Map<LocalDateTime, String> historyListToStatusesMap(List<History> historyList) {
        final Map<LocalDateTime, String> statusChangesMap = new HashMap<>();
        for (History history : historyList) {
            if (history.getStatusChangeDatetime() == null) {
                continue;
            }
            statusChangesMap.put(history.getStatusChangeDatetime(), history.getStatusDescription());
        }
        return statusChangesMap;
    }


    // upewniam się, że w tabeli historia nie ma jeszcze zapisu dla tej przesyłki z informacją o nadaniu takiego statusu
//        final Optional<History> historyOptional = historyRepository.findByDeliveryIdAndDeliveryStatus(deliveryDto.getDeliveryId(), deliveryDtoStatus);
//        if (historyOptional.isPresent()) {
//            log.debug("There is already information for delivery: deliveryDto = {} about having status = {}. No new entry is needed.",
//                    deliveryDto, deliveryDtoStatus);
//            return;
//        }
//        //TODO zwróć ostatnią pozycję na liście czas najświeższy
//        final StatusChange statusChange = statusChangeList.get(statusChangeList.size() - 1);
//        statusChange.getStatusChangeTimeStamp();
//    }

    public List<DeliveryDto> findActiveDeliversUpdatesForDeliver(Deliverer deliverer, List<Delivery> activeDeliveriesFromDatabase) {
        if (CollectionUtils.isEmpty(activeDeliveriesFromDatabase)) {
            log.debug("No active deliveries from {}. Deliveries from this deliverer won't be updated.", deliverer);
            return Collections.emptyList();
        }
        log.info("Start updating {} deliveries.", deliverer);
        final List<DeliveryDto> deliveryDtoList = new ArrayList<>();// będzie mieć statusy delivera
        log.debug("Active {} deliveries: {}.", deliverer, activeDeliveriesFromDatabase);
        activeDeliveriesFromDatabase.stream()
                .filter(Objects::nonNull)
                .forEach(delivery -> {
                    log.debug("Found active delivery in database with delivery_number = {}", delivery.getDeliveryNumber());
                    final String json = HttpCaller.callersMap
                            .get(deliverer)
                            .apply(
                                    HttpCaller.endpointUrlsMap.get(deliverer), delivery.getDeliveryNumber()
                            );
                    final DeliveryDto deliveryDto = gsonsMap.get(deliverer).fromJson(json, DeliveryDto.class);
                    deliveryDto.setDeliveryId(delivery.getDeliveryId());//TODO, czy koniecznie potrzebne?? ; jednak wygodnie jak ma - do wyszukiwania w tabeli historia - nie trzeba przekazywać osobno tej liczby
                    deliveryDtoList.add(deliveryDto);
                });
        return deliveryDtoList;
    }

    private Gson initGson(JsonDeserializer<DeliveryDto> jsonDeserializer) {
        return new GsonBuilder()
                .registerTypeAdapter(DeliveryDto.class, jsonDeserializer)
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
    }
}
