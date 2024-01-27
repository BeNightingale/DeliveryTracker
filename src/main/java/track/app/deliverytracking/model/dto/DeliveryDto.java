package track.app.deliverytracking.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import track.app.deliverytracking.mapper.Mapper;
import track.app.deliverytracking.model.Deliverer;
import track.app.deliverytracking.model.Delivery;

import java.time.LocalDateTime;
import java.util.List;

import static track.app.deliverytracking.model.DeliveryStatus.UNKNOWN;


@ToString
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class DeliveryDto {

    private int deliveryId;
    @NotNull
    private String deliveryNumber;
    private LocalDateTime deliveryCreated;
    private String deliveryStatus; // status string dokładnie tj. pobrany z api dostawcy (z jsona); może być null -> będzie zmapowany na UNKNOWN
    @Size(max = 3000)
    private String statusDescription;
    @NotNull
    private Deliverer deliverer;
    @Size(max = 2000)
    private String deliveryDescription;
    private LocalDateTime thisStatusChangeDateTime;
    private boolean finished;
    private List<StatusChange> statusChangesList;

    public Delivery toDelivery() {
        final Delivery delivery = new Delivery();
        delivery.setDeliveryId(this.deliveryId);
        delivery.setDeliveryNumber(this.deliveryNumber);
        delivery.setDeliveryCreated(this.deliveryCreated);
        delivery.setDeliveryStatus(
                this.deliveryStatus == null ?
                        UNKNOWN :
                        Mapper.statusMapperFunctions
                                .get(this.deliverer)
                                .apply(this.deliveryStatus));
        delivery.setStatusDescription(this.statusDescription);
        delivery.setStatusChangeDatetime(this.thisStatusChangeDateTime);
        delivery.setDeliverer(this.deliverer);
        delivery.setDeliveryDescription(this.deliveryDescription);
        delivery.setFinished(this.finished);
        return delivery;
    }
}
