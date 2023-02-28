package com.mercadolibre.refunds_consistency.utils;

import com.mercadolibre.refunds_consistency.constants.HeadersNames;
import com.mercadolibre.refunds_consistency.dto.RequestResponse;
import com.mercadolibre.refunds_consistency.exceptions.UnauthorizedException;
import com.mercadolibre.refunds_consistency.service.ConnectionService;
import com.mercadolibre.refunds_consistency.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ValidateAuthorizationHeaders {

    @Autowired
    private ConnectionService connectionService;
    @Autowired
    private PaymentService paymentService;

    /*public void validateTigerTokenHeader() {
        String URI_TEST = paymentService.buildOneSourceUri(12345L);
        this.connectionService.doRequestApi(
                URI_TEST,
                HttpMethod.GET,
                HeadersNames.TIGER_TOKEN_HEADER.getHeaderName(),
                HeadersNames.X_CALLER_SCOPES_HEADER.getHeaderName()
        );
        if (RequestResponse.statusResponse == HttpStatus.UNAUTHORIZED) {
            throw new UnauthorizedException("x-tiger-token header is missing or invalid");
        }
    }*/
}
