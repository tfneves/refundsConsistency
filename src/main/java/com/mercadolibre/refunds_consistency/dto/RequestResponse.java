package com.mercadolibre.refunds_consistency.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

public abstract class RequestResponse {
    public static HttpStatus statusResponse;
}
