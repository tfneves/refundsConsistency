package com.mercadolibre.refunds_consistency.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class ResponseModel {

    private Long payment_id;
    private String contingencies;
    private String contingencie_status;
    private Integer qtd_refunds_payments_api;
    private Long bank_transfer_id;
    private Integer qtd_refunds_payin_api;
    private String last_refund_id;
    private String last_refund_status;
    private String final_status_payment;
}
