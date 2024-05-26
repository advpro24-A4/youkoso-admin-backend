package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.shipment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateShipmentRequest {

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("shipment_method")
    private String shipmentMethod;
}
