package com.mercadolibre.refunds_consistency.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseModel {

    private Long payment_id;
    private Integer qtd_refunds_payments_api;
    private Long bank_transfer_id;
    private String last_refund_status;
    private ContingencieDetails contingecie_details;
    private PayinRefundDetails payin_refund_details;
    private String final_status_payment;
}
