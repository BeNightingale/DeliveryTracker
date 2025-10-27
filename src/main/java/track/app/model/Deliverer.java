package track.app.model;

import org.apache.commons.lang3.StringUtils;

public enum Deliverer {

    INPOST,
    POCZTA_POLSKA,
    DPD,
    DHL;

    public static Deliverer getDelivererFromString(String delivererName) {
        if (StringUtils.isEmpty(delivererName)) {
            return null;
        }
        for (Deliverer deliverer : Deliverer.values()) {
            if (delivererName.equals(deliverer.name())) {
                return deliverer;
            }
        }
        return null;
    }
}
