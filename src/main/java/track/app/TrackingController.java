package track.app;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import track.app.model.Deliverer;
import track.app.model.Delivery;
import track.app.model.DeliveryStatus;
import track.app.model.History;
import track.app.model.dto.DeliveryDto;
import track.app.repository.DeliveryRepository;
import track.app.repository.HistoryRepository;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TrackingController {

    private final DeliveryRepository deliveryRepository;
    private final HistoryRepository historyRepository;

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
    public String addDelivery(ModelMap model,
                              @RequestParam(name = "deliveryNumber") String deliveryNumber,
                              @RequestParam(name = "deliverer") String stringDeliverer,
                              @RequestParam(name = "deliveryDescription") String deliveryDescription) {
        try {
            // TODO zrobić walidację parametrów
            // Sprawdzenie, czy w bazie nie ma takiej przesyłki od tego dostawcy.
            final Deliverer deliverer = Deliverer.getDelivererFromString(stringDeliverer);
            System.out.printf("Deliverer: {}", deliverer);
            if (deliverer == null) {
                throw new IllegalArgumentException("Wrong deliverer name");
            }
            final Delivery deliveriesWithNumber = deliveryRepository.findDeliveryByDeliveryNumberAndDeliverer(
                        deliveryNumber, deliverer);
            if (deliveriesWithNumber != null) {
                log.debug("Delivery cannot be inserted into database because there is already a {} delivery with this number in database.", deliverer);
                System.out.println("Nie można wpisać do bazy, bo juz jest!");
                return "error";
            }
            // Jeśli nie ma, to wpisujemy do bazy danych.
            final DeliveryDto deliveryDto = DeliveryDto
                            .builder()
                            .deliveryNumber(deliveryNumber)
                            .deliverer(deliverer)
                            .deliveryCreated(LocalDateTime.now())
                            .thisStatusChangeDateTime(LocalDateTime.now())
                            .deliveryDescription(deliveryDescription)
                            .build();
            System.out.printf("DeliveryDto={}", deliveryDto);
            final Delivery delivery = deliveryRepository.save(deliveryDto.toDelivery());
            model.addAttribute("delivery", delivery);
            log.debug("Delivery inserted into database: {}.", delivery);
            System.out.printf("Delivery inserted into database: {}.", delivery);
            final History history = historyRepository.save(History.createHistoryFromDelivery(delivery));
            model.addAttribute("history", history);
            log.debug("History event inserted into database table 'history': {}.", history);
            System.out.printf("History event inserted into database table 'history': {}.", history);
            return "success";
        } catch(Exception ex) {
//            log.debug("Inserting delivery into database wasn't possible, because the parcel with the deliveryNumber {}" +
//                    " of the deliverer {}, is already tracked in database.", deliveryNumber, deliverer);
            log.error("No success in setting information about delivery with deliveryNumber = {} in database.", deliveryNumber);
            System.out.printf("No success in setting information about delivery with deliveryNumber = {} in database.", deliveryNumber);
            return "error";
        }

    }
}
