package track.app.mapper;

import org.apache.commons.lang3.StringUtils;
import track.app.model.DeliveryStatus;


public class PolishPostStatusMapper {

    private PolishPostStatusMapper() {
        // do nothing
    }

    public static DeliveryStatus toDeliveryStatusMapper(String polishPostStatusName) {
        if (StringUtils.isEmpty(polishPostStatusName)) {
            return DeliveryStatus.NOT_FOUND;
        }
        if (polishPostStatusName.toLowerCase().matches("(przygotowana|rejestracja przesyłki|otrzymano dane elektroniczne przesyłki).*")) {
            return DeliveryStatus.CONFIRMED;
        }
        if (polishPostStatusName.toLowerCase().matches("(nadana|nadanie|przesyłka przyjęta w punkcie|przyjęcie przesyłki).*")){
            return DeliveryStatus.HANDED_TO_SHIPPING_COMPANY;
        }
        if (polishPostStatusName.toLowerCase().matches("")){
            return DeliveryStatus.IN_SHIPPING_PARCEL_LOCKER;
        }
        if (polishPostStatusName.toLowerCase().matches("(w transporcie|wysłanie przesyłki|przyjęto w polsce|nadejście).*")) {
            return DeliveryStatus.ON_THE_ROAD;
        }
        if (polishPostStatusName.toLowerCase().matches("(w doręczeniu|przygotowano do doręczenia|przekazano do doręczenia).*")) {
            return DeliveryStatus.HANDED_OUT_FOR_DELIVERY;
        }
        if (polishPostStatusName.toLowerCase().matches(
                "(do odbioru w placówce|awizo przesyłki|awizo - przesyłka do odbioru|ponowne awizo|awizo - do ponownego doręczenia|powiadomienie email o oczekiwaniu przesyłki|powiadomienie sms o oczekiwaniu przesyłki).*")) {
            return DeliveryStatus.WAITING_FOR_COLLECTING;
        }
        if (polishPostStatusName.toLowerCase().matches(
                "((doręczona|doręczenie|przesyłka odebrana|odebrano|odebranie przesyłki w urzędzie).*)|(wydanie przesyłki uprawnionemu do odbioru)|(powiadomienie sms o doręczeniu przesyłki do skrzynki pocztowej)")) {
            return DeliveryStatus.DELIVERED;
        }
        return DeliveryStatus.NOT_STANDARD_STAGE;
    }
}
