package com.mercadolibre.refunds_consistency.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseModel {

    private PaymentDetails payment_details;
    private PayinDetails payin_details;
    private String final_status_payment;
}
