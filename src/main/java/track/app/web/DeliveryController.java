package track.app.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import track.app.mapper.InPostStatusMapper;
import track.app.model.Deliverer;
import track.app.model.Delivery;
import track.app.model.History;
import track.app.model.dto.DeliveryDto;
import track.app.repository.DeliveryRepository;
import track.app.repository.HistoryRepository;
import track.app.service.DeliveryService;
import track.app.service.HttpCaller;


import java.time.LocalDateTime;
import java.util.*;

import static track.app.model.Deliverer.INPOST;
import static track.app.model.Deliverer.POCZTA_POLSKA;


@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveryapi")
@Slf4j
public class DeliveryController {
    private final HistoryRepository historyRepository;

    private static final String INPOST_ENDPOINT_URL = "https://api-shipx-pl.easypack24.net/v1/tracking/";
    private static final String POLISH_POST_ENDPOINT_URL = "https://uss.poczta-polska.pl/uss/v1.1/tracking/checkmailex";

    private final DeliveryRepository deliveryRepository;
    private final DeliveryService deliveryService;
    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;


    // ------------ operacje dotyczące tylko bazy danych aplikacji DeliveryTracking ---------------------

    /**
     * @return liczba wszystkich przesyłek zapisanych w tabeli deliveries
     */
    @GetMapping("/deliveries_count")
    public long getDeliveriesCount() {
        return deliveryRepository.findAll().stream().mapToInt(d -> 1).count();
    }

    /**
     * @return lista obiektów Delivery z informacjami o wszystkich przesyłkach w tabeli deliveries.
     */
    @GetMapping("/deliveries")
    public List<Delivery> getDeliveries() {
        return deliveryRepository.findAll().stream().toList();
    }

    @GetMapping("/del")
    public Delivery getDelivery(@RequestParam(name = "deliveryId") int deliveryId) {
        return deliveryRepository.findDeliveryByDeliveryId(deliveryId);
    }

    /**
     * Wpisuje do tabeli deliveries bazy danych delivery_tracking dane nowej przesyłki,
     * pod warunkiem że w tabeli nie figuruje przesyłka o danym numerze dostarczana przez określonego w deliveryDto przewoźnika
     *
     * @param deliveryDto obiekt z informacjami o nowej przesyłce
     * @param errors      błędy walidacyjne pól obiektu deliveryDto
     * @param httpRequest zapytanie
     * @return zwraca obiekt ResponseEntity z obiektem wpisanym do bazy w przypadku powodzenia operacji i kodem odpowiedzi 200,
     * w przeciwnym razie wiadomość dotyczącą błędu i kod 400, 403 lub 500 w zależności od rodzaju błędu
     */
    @PostMapping("/deliveries")
    public ResponseEntity<Object> addDelivery(
            @Validated @RequestBody DeliveryDto deliveryDto, // tylko podstawowe pola: numer_przesyłki, dostawca, opis, co zawiera przesyłka, status przewoźnika lub null, jeśli nieznany
            Errors errors,
            HttpServletRequest httpRequest) {
        if (deliveryDto == null) {
            log.debug("No delivery information provided, deliveryDto = null.");
            return ResponseEntity.badRequest().body("No delivery information provided.");
        }
        Locale locale = localeResolver.resolveLocale(httpRequest);
        if (errors.hasErrors()) {
            String message = errors.getAllErrors().stream()
                    .map(oe -> messageSource.getMessage(Objects.requireNonNull(oe.getCode()), oe.getArguments(), locale))
                    .reduce("errors:\n", (accu, error) -> accu + error + "\n");
            return ResponseEntity.badRequest().body(message);
        }
        try {
            final String deliveryNumber = deliveryDto.getDeliveryNumber();
            final Deliverer deliverer = deliveryDto.getDeliverer();
            // Sprawdzenie, czy w bazie nie ma takiej przesyłki od tego dostawcy.
            final Delivery deliveriesWithNumber = deliveryRepository.findDeliveryByDeliveryNumberAndDeliverer(
                    deliveryNumber, deliverer);
            if (deliveriesWithNumber == null) {
                // Jeśli nie ma, to wpisujemy do bazy danych.
                deliveryDto.setDeliveryCreated(LocalDateTime.now());
                deliveryDto.setStatusChangeDateTime(LocalDateTime.now());
                deliveryDto.setFinished(false);
                final Delivery delivery = deliveryRepository.save(deliveryDto.toDelivery());
                log.debug("Delivery inserted into database: {}.", delivery);
                final History history = historyRepository.save(History.createHistoryFromDelivery(delivery));
                log.debug("History event inserted into database table 'history': {}.", history);
                return ResponseEntity.ok(delivery);
            }
            log.debug("Inserting delivery into database wasn't possible, because the parcel with the deliveryNumber {}" +
                    " of the deliverer {}, is already tracked in database.", deliveryNumber, deliverer);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(String.format(
                            "The parcel with the deliveryNumber %s of the deliverer %s is already tracked in database.",
                            deliveryNumber, deliverer)
                    );
        } catch (Exception ex) {
            log.error("No success in setting information about delivery with deliveryNumber = {} in database.", deliveryDto.getDeliveryNumber());
            return ResponseEntity.internalServerError().body("No success in setting information about delivery in database.");
        }
    }

    @GetMapping("/deliveries/{deliveryNumber}/{deliverer}") // wyszukuje w bazie apki=tracker
    public Delivery getDeliveryByNumber(@PathVariable String deliveryNumber, @PathVariable Deliverer deliverer) {
        final Delivery delivery = deliveryRepository.findDeliveryByDeliveryNumberAndDeliverer(deliveryNumber, deliverer);
        log.debug("Found: {}", delivery);
        return delivery;
    }

    @DeleteMapping("/deliveries/{deliveryNumber}/{deliverer}")
    public ResponseEntity<Object> deleteDeliveryByDeliveryNumberAndDeliverer(@PathVariable String deliveryNumber, @PathVariable Deliverer deliverer) {
        log.debug("Init - delivery to delete: deliveryNumber = {}, deliverer = {}.", deliveryNumber, deliverer);
        final Delivery delivery = deliveryRepository.findDeliveryByDeliveryNumberAndDeliverer(deliveryNumber, deliverer);
        if (delivery == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(String.format("Deleting delivery with deliveryNumber %s isn't possible. The delivery not found in database.", deliveryNumber));
        }
        try {
            final int changedRowsNumber = deliveryRepository.deleteByDeliveryNumberAndDeliverer(deliveryNumber, deliverer);
            log.debug("Number of changed rows in database table: {}", changedRowsNumber);
            if (changedRowsNumber == 1) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body(String.format("Deleted delivery with deliveryNumber %s.", deliveryNumber));
            } else {
                return ResponseEntity
                        .internalServerError()
                        .body(String.format("No success in deleting delivery with deliveryNumber %s. Internal exception.", deliveryNumber));
            }
        } catch (Exception ex) {
            log.debug("Exception occurred:", ex);
            return ResponseEntity
                    .internalServerError()
                    .body(String.format("No success in deleting delivery with deliveryNumber %s. Internal exception.", deliveryNumber));
        }
    }

// ----------------------- endpointy do integracji z różnymi dostawcami przesyłek -------------------------


    // ------------- POCZTA POLSKA --------------------

    /**
     * Wyszukuje w bazie Poczty Polskiej przesyłkę o określonym numerze.
     *
     * @param deliveryNumber numer przesyłki (umieszczany w polu przesyłanego obiektu z dodatkowymi informacjami o języku
     *                       i addPostOfficeInfo (czy odpowiedź ma zawierać informacje o przesyłce)
     * @return json z informacjami o przesyłce
     */
    @GetMapping("/json_polish_deliveries/{deliveryNumber}")
    public String getJsonDeliveryInformationFromPolishPost(@PathVariable String deliveryNumber) {
        return HttpCaller.callHttpPostMethod(HttpCaller.endpointUrlsMap.get(POCZTA_POLSKA), deliveryNumber);
    }

    /**
     * Wyszukuje w bazie Poczty Polskiej przesyłkę o określonym numerze.
     *
     * @param deliveryNumber numer przesyłki (umieszczany w polu przesyłanego obiektu z dodatkowymi informacjami o języku
     *                       i addPostOfficeInfo (czy odpowiedź ma zawierać informacje o przesyłce)
     * @return obiekt DeliveryDto z informacjami o przesyłce z listą - historią jej statusów Poczty Polskiej
     */
    @GetMapping("/polish_deliveries/{deliveryNumber}")
    public DeliveryDto getDeliveryInformationFromPolishPost(@PathVariable String deliveryNumber) {
        final String json = HttpCaller.callHttpPostMethod(HttpCaller.endpointUrlsMap.get(POCZTA_POLSKA), deliveryNumber);
        return deliveryService.getPolishPostGson().fromJson(json, DeliveryDto.class);
    }


// ------------------------- INPOST -----------------------------------------------

    /**
     * Wyszukuje w bazie InPost przesyłkę o określonym numerze.
     *
     * @param deliveryNumber numer szukanej przesyłki
     * @return json z pełnymi informacjami o przesyłce z api InPost
     */
    @GetMapping("/json_inpost_deliveries/{deliveryNumber}")
    public String getJsonDeliveryInformationFromInPost(@PathVariable String deliveryNumber) {
        return HttpCaller.callHttpGetMethod(INPOST_ENDPOINT_URL, deliveryNumber);
    }

    /**
     * Wyszukuje w bazie InPost przesyłkę o określonym numerze.
     *
     * @param deliveryNumber numer szukanej przesyłki
     * @return obiekt DeliveryDto z pełnymi informacjami o przesyłce z api InPost (z listą - historią jej InPostowych statusów)
     */
    @GetMapping("/inpost_deliveries/{deliveryNumber}")
    public DeliveryDto getDeliveryInformationFromInPost(@PathVariable String deliveryNumber) {
        String json = HttpCaller.callHttpGetMethod(INPOST_ENDPOINT_URL, deliveryNumber);
        return deliveryService.getInPostGson().fromJson(json, DeliveryDto.class);
    }

    /**
     * Pobiera z tabeli deliveries numery wszystkich przesyłek o aktywnych statusach (czyli przesyłek niedostarczonych),
     * a następnie — na podstawie informacji pobranych z api InPost lub api Poczty Polskiej — aktualizuje im statusy.
     *
     * @return liczba zmienionych wierszy w tabeli deliveries
     */
    @GetMapping("/current_statuses") // aktualizuje w bazie statusy wszystkim aktywnym przesyłkom INPOST lub Poczty Polskiej
    public ResponseEntity<Integer> getDeliveriesWithActiveStatusesAndUpdate() {
        final List<Delivery> inPostActiveDeliveries = deliveryRepository
                .findByDelivererAndDeliveryStatusIn(INPOST, InPostStatusMapper.getActiveStatusesList());
        final List<Delivery> polishPostActiveDeliveries = deliveryRepository
                .findByDelivererAndFinishedIsFalse(POCZTA_POLSKA);
        if (
                CollectionUtils.isEmpty(inPostActiveDeliveries)
                &&
                CollectionUtils.isEmpty(polishPostActiveDeliveries)
        ) {
            log.debug("No deliveries with active statuses in database - nothing to update.");
            return ResponseEntity.noContent().build();
        }
        final List<DeliveryDto> deliveryDtoList = new ArrayList<>();// będzie mieć statusy delivera
        deliveryDtoList.addAll(deliveryService.findActiveDeliversUpdatesForDeliver(INPOST, inPostActiveDeliveries));
        deliveryDtoList.addAll(deliveryService.findActiveDeliversUpdatesForDeliver(POCZTA_POLSKA, polishPostActiveDeliveries));
        final Integer rowsChanged = deliveryService.updateActiveDeliveriesStatuses(deliveryDtoList);
        return ResponseEntity.ok().body(rowsChanged);
    }
}
