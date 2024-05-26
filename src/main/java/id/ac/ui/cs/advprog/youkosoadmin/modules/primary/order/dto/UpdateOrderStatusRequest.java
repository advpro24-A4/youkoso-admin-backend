package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateOrderStatusRequest {

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("order_status")
    private String orderStatus;

}
