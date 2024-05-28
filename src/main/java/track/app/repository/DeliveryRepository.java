package track.app.repository;

import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import track.app.model.Deliverer;
import track.app.model.Delivery;
import track.app.model.DeliveryStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {

    List<Delivery> findDeliveryByDeliveryStatus(@NonNull DeliveryStatus deliveryStatus);

    Optional<Delivery> findDeliveryByDeliveryNumber(@NonNull String deliveryNumber);

    List<Delivery> findByDeliveryStatusIn(List<DeliveryStatus> deliveryStatusList);

    @Override
    @Nonnull Page<Delivery> findAll(@NonNull Pageable pageable);

    Delivery findDeliveryByDeliveryId(@NonNull int deliveryId);

    List<Delivery> findByDelivererAndDeliveryStatusIn(@NonNull Deliverer deliverer, List<DeliveryStatus> deliveryStatusList);
    List<Delivery> findByDelivererAndFinishedIsFalse(@NonNull Deliverer deliverer);

    Delivery findDeliveryByDeliveryNumberAndDeliverer(@NonNull String deliveryNumber, @NonNull Deliverer deliverer);

    @Transactional
    int deleteByDeliveryNumberAndDeliverer(@NonNull String deliveryNumber, @NonNull Deliverer deliverer);

    @Transactional
    int deleteByDeliveryId(int id);
}
