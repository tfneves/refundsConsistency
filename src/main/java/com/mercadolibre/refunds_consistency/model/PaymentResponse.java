package com.mercadolibre.refunds_consistency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResponse {

    private List<Payment> payments;
}
