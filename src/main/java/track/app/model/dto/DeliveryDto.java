package track.app.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import track.app.mapper.Mapper;
import track.app.model.Deliverer;
import track.app.model.Delivery;

import java.time.LocalDateTime;
import java.util.List;

import static track.app.model.DeliveryStatus.UNKNOWN;


@ToString
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class DeliveryDto {

    private Integer deliveryId;
    @NotBlank
    private String deliveryNumber;
    private LocalDateTime deliveryCreated;
    private String deliveryStatus; // status string dokładnie tj. pobrany z api dostawcy (z jsona); może być null -> będzie zmapowany na UNKNOWN
    @Size(max = 3000)
    private String statusDescription;
    @NotNull
    private Deliverer deliverer;
    @Size(max = 200)
    private String deliveryDescription;
    private LocalDateTime thisStatusChangeDateTime;
    private Boolean finished;
    private List<StatusChange> statusChangesList;

    public Delivery toDelivery() {
        final Delivery delivery = new Delivery();
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
