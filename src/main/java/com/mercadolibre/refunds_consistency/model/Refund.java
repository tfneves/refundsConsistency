package com.mercadolibre.refunds_consistency.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Refund {

    private Double amount;
    private String date_created;
    private String date_last_updated;
    private String id;
    private String refund_mode;
    private String status;
    private String reason;
    private String rejected_reason;
    private Boolean pending;
    private String external_id;
    private String return_id;
    private Boolean approved;
}
