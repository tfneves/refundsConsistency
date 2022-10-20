package com.mercadolibre.refunds_consistency.exceptions;

import org.springframework.web.client.RestClientException;

public class ConnectionTimeoutException extends RestClientException {

    public ConnectionTimeoutException(String msg) {
        super(msg);
    }
}
