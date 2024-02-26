package track.app;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;
import track.app.mapper.InPostStatusMapper;
import track.app.model.Deliverer;
import track.app.model.dto.DeliveryDto;
import track.app.model.dto.StatusChange;


import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

import static track.app.DeserializerHelper.getAsStringOrNull;


public class InPostJsonDeserializer implements JsonDeserializer<DeliveryDto> {


    @Override
    public DeliveryDto deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        if (jsonElement == null) {
            return null;
        }
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject == null) {
            return null;
        }
        final JsonElement deliveryNumberElem = jsonObject.get("tracking_number");
        final String deliveryNumber = getAsStringOrNull(deliveryNumberElem);
        final JsonElement statusElem = jsonObject.get("status");
        final String status = getAsStringOrNull(statusElem);
        final JsonArray trackingDetailsArray = jsonObject.getAsJsonArray("tracking_details");
        List<StatusChange> statusChangesList;
        if (trackingDetailsArray == null) {
            statusChangesList = Collections.emptyList();
        } else {
            statusChangesList = new ArrayList<>();
            for (int i = 0; i < trackingDetailsArray.size(); i++) {
                final JsonObject arrayObject = trackingDetailsArray.get(i).getAsJsonObject();
                if (arrayObject == null) {
                    continue;
                }
                final JsonElement changedStatusElem = arrayObject.get("status");
                final String changedStatus = getAsStringOrNull(changedStatusElem);
                final JsonElement changeTimestampElem = arrayObject.get("datetime");
                final String changeTimestamp = getAsStringOrNull(changeTimestampElem);
                final StatusChange statusChange = new StatusChange(
                        changedStatus,
                        DeserializerHelper.parseStringToLocalDateTime(changeTimestamp.substring(0, 23))
                );
                statusChangesList.add(statusChange);
            }
            statusChangesList.sort(Comparator.comparing(StatusChange::getStatusChangeTimeStamp));
        }
        final LocalDateTime thisStatusChangeDateTime = getThisStatusChangeDateTime(statusChangesList, status);
        // Statusy od przewoźnika! Ma to, co przynosi json.
        return DeliveryDto.builder()
                .deliveryNumber(deliveryNumber)
                .deliveryStatus(status)
                .statusDescription(InPostStatusMapper.inPostShortDescriptionStatusMap.get(status))
                .statusChangesList(statusChangesList)
                .deliverer(Deliverer.INPOST)
                .statusChangeDateTime(thisStatusChangeDateTime)
                .finished("delivered".equals(status))//TODO, czy może jeszcze inne szczególne sytuacje?
                .build();
    }

    private LocalDateTime getThisStatusChangeDateTime(List<StatusChange> statusChangesList, String status) {
        if (StringUtils.isEmpty(status))
            return null;
        return statusChangesList.stream()
                .filter(statusChange -> Objects.equals(status, statusChange.getStatus()))
                .map(StatusChange::getStatusChangeTimeStamp)
                .findFirst().orElse(null);
    }
}
