package com.mercadolibre.refunds_consistency.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class PaymentDTO {

    @NotNull(message = "Payment id is empty!")
    private Long payment_id;
}
