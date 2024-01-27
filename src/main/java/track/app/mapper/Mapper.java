package track.app.mapper;


import track.app.model.Deliverer;
import track.app.model.DeliveryStatus;

import java.util.Map;
import java.util.function.Function;


public class Mapper {

    private Mapper() {
        // do nothing
    }

    private static final Function<String, DeliveryStatus> inPostMapFunction = InPostStatusMapper::toDeliveryStatusMapper;
    private static final Function<String, DeliveryStatus> polishPostMapFunction = PolishPostStatusMapper::toDeliveryStatusMapper;

    public static final Map<Deliverer, Function<String, DeliveryStatus>> statusMapperFunctions = Map.of(
            Deliverer.INPOST, inPostMapFunction,
            Deliverer.POCZTA_POLSKA, polishPostMapFunction
    );
}
