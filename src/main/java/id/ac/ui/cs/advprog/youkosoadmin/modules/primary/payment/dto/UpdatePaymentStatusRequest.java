package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePaymentStatusRequest {
    @JsonProperty("payment_id")
    private Long paymentId;
}
