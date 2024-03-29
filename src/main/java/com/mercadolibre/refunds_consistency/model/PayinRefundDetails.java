package com.mercadolibre.refunds_consistency.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayinRefundDetails {

    private Integer qtd_refunds;
    private String last_refund_id;
    private Boolean is_partial_refund;
    private Double amount;
}
