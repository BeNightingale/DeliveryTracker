package track.app.deliverytracking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import track.app.deliverytracking.repository.DeliveryRepository;
import track.app.deliverytracking.repository.HistoryRepository;

@Controller
@RequiredArgsConstructor
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
}
