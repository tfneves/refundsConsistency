package com.mercadolibre.refunds_consistency.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRefundDetails {

    private Integer qtd_refunds;
    private String last_refund_status;
}
