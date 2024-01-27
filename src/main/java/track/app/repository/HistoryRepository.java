package track.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import track.app.model.DeliveryStatus;
import track.app.model.History;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Integer> {

    Optional<History> findByDeliveryIdAndDeliveryStatus(int deliveryId, DeliveryStatus deliveryStatus);
    List<History> findHistoryByDeliveryId(@NonNull int deliveryId);

}
