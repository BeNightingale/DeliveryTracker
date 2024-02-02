package track.app;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import track.app.model.Delivery;
import track.app.model.History;
import track.app.model.dto.DeliveryDto;
import track.app.repository.DeliveryRepository;
import track.app.repository.HistoryRepository;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TrackingController {

    private static final String ERROR = "error";

    private final DeliveryRepository deliveryRepository;
    private final HistoryRepository historyRepository;
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

    @GetMapping("/add")
    public String addDelivery(ModelMap model, @Validated DeliveryDto deliveryDto, Errors errors, HttpServletRequest httpRequest) {
        try {
            log.error("DeliveryDto={}", deliveryDto);
            Locale locale = localeResolver.resolveLocale(httpRequest);
            if (errors.hasErrors()) {
                log.error("Są błędy");
                log.error("Liczba błedów {}", errors.getErrorCount());
                log.error("Nazwa błędu1 {}", errors.getAllErrors().get(0).getObjectName());
                log.error("Lista błedów {}", errors.getAllErrors().toString());
                final String message = errors.getAllErrors().stream()
                        .map(oe -> messageSource.getMessage(Objects.requireNonNull(oe.getCode()), oe.getArguments(), locale))
                        .reduce("errors:\n", (accu, error) -> accu + error + "\n");
                model.addAttribute("message", message);
                return ERROR;
            }
            final Delivery deliveriesWithNumber = deliveryRepository.findDeliveryByDeliveryNumberAndDeliverer(
                    deliveryDto.getDeliveryNumber(), deliveryDto.getDeliverer());
            if (deliveriesWithNumber != null) {
                log.error("Delivery cannot be inserted into database because there is already {} delivery with this number in database.",
                        deliveryDto.getDeliverer());
                model.addAttribute("message", String.format("Delivery cannot be inserted into database because there is already %s delivery with this number in database.",
                        deliveryDto.getDeliverer()));
                return ERROR;
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
            return "success";
        } catch (Exception ex) {
            log.error("No success in setting information about delivery with deliveryNumber = {} in database.", deliveryDto.getDeliveryNumber());
            return ERROR;
        }
    }

    @GetMapping("/deleting")
    public String deleteDelivery(@RequestParam int deliveryId) {
        int changedRowsNumber = deliveryRepository.deleteByDeliveryId(deliveryId);
        log.error("Changed row number {} Deleted deliveryId={}.", changedRowsNumber, deliveryId);
        return "success";
    }
}
