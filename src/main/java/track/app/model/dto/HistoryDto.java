package track.app.model.dto;

import lombok.Builder;
import track.app.model.DeliveryStatus;
import track.app.model.History;

import java.time.LocalDateTime;

@Builder
public class HistoryDto {

    private int id;
    private int deliveryId;
    // Powinno być prawie unikatowe i niepuste, ale dla różnych dostawców może się powtórzyć.
    private String deliveryNumber;
    private DeliveryStatus deliveryStatus;
    private String statusDescription;
    private LocalDateTime statusChangeDateTime;

    public History toHistory() {
        final History history = new History();
        history.setDeliveryId(this.deliveryId);
        history.setDeliveryNumber(this.deliveryNumber);
        history.setDeliveryStatus(this.deliveryStatus);
        history.setStatusDescription(this.statusDescription);
        history.setStatusChangeDatetime(this.statusChangeDateTime);
        return history;
    }
}
