package com.mercadolibre.refunds_consistency.model;

import lombok.Data;

@Data
public class Refund {

    private Double amount;
    private String date_created;
    private String date_last_updated;
    private String id;
    private String refund_mode;
    private String status;
    private String reason;
    private String external_id;
    private Boolean pending;
    private Boolean approved;
    private String return_id;
}
