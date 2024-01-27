package track.app.deliverytracking.mapper;


import track.app.deliverytracking.model.Deliverer;
import track.app.deliverytracking.model.DeliveryStatus;

import java.util.Map;
import java.util.function.Function;

import static track.app.deliverytracking.model.Deliverer.INPOST;
import static track.app.deliverytracking.model.Deliverer.POCZTA_POLSKA;


public class Mapper {

    private Mapper() {
        // do nothing
    }

    private static final Function<String, DeliveryStatus> inPostMapFunction = InPostStatusMapper::toDeliveryStatusMapper;
    private static final Function<String, DeliveryStatus> polishPostMapFunction = PolishPostStatusMapper::toDeliveryStatusMapper;

    public static final Map<Deliverer, Function<String, DeliveryStatus>> statusMapperFunctions = Map.of(
            INPOST, inPostMapFunction,
            POCZTA_POLSKA, polishPostMapFunction
    );
}
