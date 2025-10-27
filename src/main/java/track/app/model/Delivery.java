package track.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@ToString
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private int deliveryId;
    @Column(name = "delivery_number", nullable = false, length = 30) // powinno być prawie unikatowe i niepuste!
    private String deliveryNumber;
    @Column(name = "delivery_created")
    private LocalDateTime deliveryCreated;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "delivery_status", nullable = false, length = 350)
    private DeliveryStatus deliveryStatus;
    @Column(name = "status_description", length = 3000)
    private String statusDescription;
    @Column(name = "status_change_datetime")
    private LocalDateTime statusChangeDatetime;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "deliverer", nullable = false, length = 100)
    private Deliverer deliverer;
    @Column(name = "delivery_description", length = 2000)
    private String deliveryDescription;
    @Column(name = "finished", nullable = false)
    private Boolean finished;
}
