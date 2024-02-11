package track.app;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import track.app.mapper.InPostStatusMapper;
import track.app.model.Deliverer;
import track.app.model.Delivery;
import track.app.model.History;
import track.app.model.dto.DeliveryDto;
import track.app.repository.DeliveryRepository;
import track.app.repository.HistoryRepository;
import track.app.service.DeliveryService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static track.app.model.Deliverer.INPOST;
import static track.app.model.Deliverer.POCZTA_POLSKA;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TrackingController {

    private static final String ERROR = "error";

    private final DeliveryRepository deliveryRepository;
    private final HistoryRepository historyRepository;
    private final DeliveryService deliveryService;
    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;

    //TODO delete, update manually, check if history doesn't repeat, view of success and error, zamykanie modalu przy powrocie(post/get ? przy zmianie strony pomoże?)
    //TODO stronicowanie, sortowanie
    @GetMapping("/")
    public String home() {
        return "root";
    }

    @GetMapping("/count")
    public String getCount(ModelMap modelMap) {
        modelMap.addAttribute("count", deliveryRepository.findAll().stream().mapToInt(d -> 1).count());
        return "count";
    }

    @GetMapping("/deliver")
    public String getDeliveries(ModelMap modelMap) {
        modelMap.addAttribute("deliveries", deliveryRepository.findAll().stream().toList());
        return "deliveryList";
    }

    @GetMapping("/delivery")
    public String getDelivery(ModelMap modelMap, @RequestParam(name = "deliveryId") int deliveryId) {
        modelMap.addAttribute("delivery", deliveryRepository.findDeliveryByDeliveryId(deliveryId));
        modelMap.addAttribute("historyList", historyRepository.findHistoryByDeliveryId(deliveryId));
        return "delivery";
    }

    @PostMapping(value = "/add", consumes = "application/json")
    public String addDelivery(ModelMap model, @Validated @RequestBody DeliveryDto deliveryDto, Errors errors, HttpServletRequest httpRequest, HttpServletResponse httpServletResponse) throws IOException {
        try {
            log.error("DeliveryDto={}", deliveryDto);
            Locale locale = localeResolver.resolveLocale(httpRequest);
            if (errors.hasErrors()) {
                log.error("Są błędy");
                log.error("Liczba błędów {}", errors.getErrorCount());
                log.error("Nazwa błędu1 {}", errors.getAllErrors().get(0).getObjectName());
                log.error("Lista błędów {}", errors.getAllErrors());
                final String message = errors.getAllErrors().stream()
                        .map(oe -> messageSource.getMessage(Objects.requireNonNull(oe.getCode()), oe.getArguments(), locale))
                        .reduce("", (accu, error) -> String.format("%s%s%n", accu, error));
                httpServletResponse.sendError(400, message);
                log.error("message z propertisów: " + message);
                return "deliveryList";
            }
            final  String deliveryNumber = deliveryDto.getDeliveryNumber();
            final Deliverer deliverer = deliveryDto.getDeliverer();
            final Delivery deliveriesWithNumber = deliveryRepository.findDeliveryByDeliveryNumberAndDeliverer(
                    deliveryNumber, deliverer);
            if (deliveriesWithNumber != null) {
                log.error("Delivery cannot be inserted into database because there is already {} delivery with this number in database.",
                        deliveryDto.getDeliverer());
                httpServletResponse.sendError(400, String.format("Delivery cannot be inserted into database because there is already %s delivery with number %s in database.",
                        deliverer, deliveryNumber));
                return "deliveryList";
            }
            // Jeśli nie ma, to wpisujemy do bazy danych.
            final DeliveryDto deliveryDto1 = DeliveryDto
                    .builder()
                    .deliveryNumber(deliveryDto.getDeliveryNumber())
                    .deliverer(deliveryDto.getDeliverer())
                    .deliveryCreated(LocalDateTime.now())
                    .thisStatusChangeDateTime(LocalDateTime.now())
                    .deliveryDescription(deliveryDto.getDeliveryDescription())
                    .finished(false)
                    .build();
            log.error("Nowe deliveryDto1={}", deliveryDto1);
            final Delivery delivery = deliveryRepository.save(deliveryDto1.toDelivery());
            model.addAttribute("delivery", delivery);
            log.error("Delivery inserted into database: {}.", delivery);
            final History history = historyRepository.save(History.createHistoryFromDelivery(delivery));
            model.addAttribute("history", history);
            log.error("History event inserted into database table 'history': {}.", history);
            return "deliveryList";
        } catch (Exception ex) {
            log.error("Error: ", ex);
            log.error("No success in setting information about delivery with deliveryNumber = {} in database.", deliveryDto.getDeliveryNumber());
            httpServletResponse.sendError(400, String.format("No success in setting information about delivery with deliveryNumber = %s in database.", deliveryDto.getDeliveryNumber()));
            return "deliveryList";
        }
    }

    /**
     * Endpoint służy do usuwania informacji o przesyłce z tabeli deliveries oraz historii śledzenia zmian jej statusów z tabeli history.
     * @param deliveryId - identyfikator przesyłki
     * @return - po wykonaniu operacji trigger uruchamia modal z informacją o powodzeniu lub niepowodzeniu usuwania danych
     */
    @PostMapping("/deletions")
    public String deleteDelivery(@RequestParam int deliveryId) {
        int changedDeliveriesRowsNumber = deliveryRepository.deleteByDeliveryId(deliveryId);
        log.error("Changed row number in 'deliveries' table = {}. Deleted deliveryId = {}.", changedDeliveriesRowsNumber, deliveryId);
        int changedHistoryRowsNumber = historyRepository.deleteByDeliveryId(deliveryId);
        log.error("Changed row number in 'history' table = {}. Deleted deliveryId = {}.", changedHistoryRowsNumber, deliveryId);
        return "deliveryList";
    }

    @GetMapping("/updates") // aktualizuje w bazie statusy wszystkim aktywnym przesyłkom INPOST lub Poczty Polskiej
    public String getDeliveriesWithActiveStatusesAndUpdate(ModelMap modelMap) {
        final List<Delivery> inPostActiveDeliveries = deliveryRepository
                .findByDelivererAndDeliveryStatusIn(INPOST, InPostStatusMapper.getActiveStatusesList());
        final List<Delivery> polishPostActiveDeliveries = deliveryRepository
                .findByDelivererAndFinishedIsFalse(POCZTA_POLSKA);
        int rowsChanged = 0;
        if (
                CollectionUtils.isEmpty(inPostActiveDeliveries)
                &&
                CollectionUtils.isEmpty(polishPostActiveDeliveries)
        ) {
            log.debug("No deliveries with active statuses in database - nothing to update.");
            modelMap.addAttribute("rowsChanged", rowsChanged);
            return "success";
        }
        final List<DeliveryDto> deliveryDtoList = new ArrayList<>();// będzie mieć statusy delivera
        deliveryDtoList.addAll(deliveryService.findActiveDeliversUpdatesForDeliver(INPOST, inPostActiveDeliveries));
        deliveryDtoList.addAll(deliveryService.findActiveDeliversUpdatesForDeliver(POCZTA_POLSKA, polishPostActiveDeliveries));
        rowsChanged = deliveryService.updateActiveDeliveriesStatuses(deliveryDtoList);
        modelMap.addAttribute("rowsChanged", rowsChanged);
        return "success";
    }
}
