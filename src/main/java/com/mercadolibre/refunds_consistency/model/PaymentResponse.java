package com.mercadolibre.refunds_consistency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResponse {

    private List<Payment> payments;
}
