package track.app.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@ToString
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "delivery_id", nullable = false)
    private int deliveryId;
    @Column(name = "delivery_number", nullable = false, length = 30)
    // Powinno być prawie unikatowe i niepuste, ale dla różnych dostawców może się powtórzyć.
    private String deliveryNumber;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "delivery_status", nullable = false, length = 350)
    private DeliveryStatus deliveryStatus;
    @Column(name = "status_description", length = 3000)
    private String statusDescription;
    @Column(name = "status_change_datetime")
    private LocalDateTime statusChangeDatetime;

    public static History createHistoryFromDelivery(Delivery delivery) {
        if (delivery == null) {
            return null;
        }
        return History
                .builder()
                .deliveryId(delivery.getDeliveryId())
                .deliveryNumber(delivery.getDeliveryNumber())
                .deliveryStatus(delivery.getDeliveryStatus())
                .statusDescription(delivery.getStatusDescription())
                .statusChangeDatetime(delivery.getStatusChangeDatetime())
                .build();
    }
}
