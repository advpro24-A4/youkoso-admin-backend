package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.shipment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ArriveShipmentRequest {
    @JsonProperty("order_id")
    private Long orderId;
}
