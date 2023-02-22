package com.mercadolibre.refunds_consistency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayinResponse {

    private Object transaction_id;
    private Object transaction_type;
    private Object transaction_concept;
    private Object flow;
    private Object site;
    private Object payment_method_id;
    private Object payment_id;
    private Object operation_type;
    private Object transaction_amount;
    private Object refunded_amount;
    private Object description;
    private Object collector;
    private Object payer;
    private Object currency_id;
    private Object date_approved;
    private Object date_created;
    private Object date_last_updated;
    private Object payment_type_id;
    private Object product_id;
    private List<Refund> refunds;
    private Object financial_institution;
    private Object status;
    private Object status_detail;
    private Object application_id;
    private Object application_prefix;
    private Object version;
    private Object external_reference;
    private Object payin_type_id;
    private Object same_owner;
    private Object internal_metadata;
    private Object error_info;
    private Object reason;
    private Object meli_session_id;
    private Object api_version;
    private Object transaction_detail;
    private Object id;
    private Object type;
    private Object payin_id;
}
