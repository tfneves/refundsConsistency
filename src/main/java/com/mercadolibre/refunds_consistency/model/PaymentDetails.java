package com.mercadolibre.refunds_consistency.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {

    private Long id;
    private Long bank_transfer_id;
    private ContingencieDetails contingencie_details;
    private PaymentRefundDetails refund_details;
}
