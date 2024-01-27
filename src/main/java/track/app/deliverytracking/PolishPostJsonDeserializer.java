package track.app.deliverytracking;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import track.app.deliverytracking.model.Deliverer;
import track.app.deliverytracking.model.dto.DeliveryDto;
import track.app.deliverytracking.model.dto.StatusChange;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

import static track.app.deliverytracking.DeserializerHelper.getAsStringOrNull;

public class PolishPostJsonDeserializer implements JsonDeserializer<DeliveryDto> {
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
        final JsonElement numberElem = jsonObject.get("number");
        final String deliveryNumber = getAsStringOrNull(numberElem);
        final JsonObject mailInfoObject = jsonObject.getAsJsonObject("mailInfo");
        final JsonElement finishedElem = mailInfoObject == null ? null : mailInfoObject.get("finished");
        final boolean finished = finishedElem != null && finishedElem.getAsBoolean();
        final JsonArray eventsArray = mailInfoObject == null ? null : mailInfoObject.getAsJsonArray("events");
        List<StatusChange> statusChangesList;
        if (eventsArray == null) {
            statusChangesList = Collections.emptyList();
        } else {
            statusChangesList = new ArrayList<>();
            for (int i = 0; i < eventsArray.size(); i++) {
                final JsonObject arrayObject = eventsArray.get(i).getAsJsonObject();
                if (arrayObject == null) {
                    continue;
                }
                final JsonElement nameElem = arrayObject.get("name");
                final String changedStatus = getAsStringOrNull(nameElem);
                final JsonElement changeTimestampElem = arrayObject.get("time");
                final String changeTimestamp = getAsStringOrNull(changeTimestampElem);
                final StatusChange statusChange = new StatusChange(
                        changedStatus,
                        DeserializerHelper.parseStringToLocalDateTime(changeTimestamp)
                );
                statusChangesList.add(statusChange);
            }
            statusChangesList.sort(Comparator.comparing(StatusChange::getStatusChangeTimeStamp));// rosnąco ustawia czas, czyli ostatnie miejsce ma najświeższy
        }
        final StatusChange  currentStatusChange = CollectionUtils.isEmpty(statusChangesList) ? null : statusChangesList.get(statusChangesList.size() - 1);
//        final String currentStatus = statusChangesList.isEmpty() ?
//                null :
//                statusChangesList
//                        .get(statusChangesList.size() - 1)
//                        .getStatus();
        final String status = currentStatusChange == null ? StringUtils.EMPTY : currentStatusChange.getStatus();
        final LocalDateTime thisStatusChangeDateTime = currentStatusChange == null ? null : currentStatusChange.getStatusChangeTimeStamp();
        // Wpisuje status przekazany przez Pocztę Polską = krótki opis statusu.
        // Statusy mają nazwy tj. statusy Poczty Polskiej.
        return DeliveryDto.builder()
                .deliveryNumber(deliveryNumber)
                .deliveryStatus(status)
                .statusDescription(status)
                .statusChangesList(statusChangesList)
                .deliverer(Deliverer.POCZTA_POLSKA)
                .thisStatusChangeDateTime(thisStatusChangeDateTime)
                .finished(finished)
                .build();
    }
}
