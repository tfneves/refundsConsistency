package com.mercadolibre.refunds_consistency.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Component
public class PaymentDTO {

    @NotNull(message = "Payment id is empty!")
    private Long payment_id;
}
