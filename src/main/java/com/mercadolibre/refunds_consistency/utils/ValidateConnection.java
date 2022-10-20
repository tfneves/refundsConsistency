package com.mercadolibre.refunds_consistency.utils;

import com.mercadolibre.refunds_consistency.constants.ConnectionConstants;
import com.mercadolibre.refunds_consistency.constants.HeadersNames;
import com.mercadolibre.refunds_consistency.dto.RequestResponse;
import com.mercadolibre.refunds_consistency.exceptions.ConnectionTimeoutException;
import com.mercadolibre.refunds_consistency.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ValidateConnection {

    @Autowired
    private ConnectionService connectionService;

    public void validateVPNConnection() {
        String URI_FURY_PING = ConnectionConstants.URI_TEST_VPN;
        this.connectionService.doRequestApi(
                URI_FURY_PING,
                HttpMethod.GET,
                HeadersNames.FURY_HEADER.getHeaderName()
        );
        if (RequestResponse.statusResponse == HttpStatus.REQUEST_TIMEOUT) {
            throw new ConnectionTimeoutException("Please check your VPN connection or network");
        }
    }
}
