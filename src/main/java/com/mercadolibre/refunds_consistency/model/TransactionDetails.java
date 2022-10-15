package com.mercadolibre.refunds_consistency.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDetails {

    private String acquirer_reference;
    private Long bank_transfer_id;
    private String external_resource_url;
    private String financial_institution;
    private Integer installment_amount;
    private BigDecimal net_received_amount;
    private Integer overpaid_amount;
    private Object payable_deferral_period;
    private Object payment_method_reference_id;
    private BigDecimal total_paid_amount;
}
