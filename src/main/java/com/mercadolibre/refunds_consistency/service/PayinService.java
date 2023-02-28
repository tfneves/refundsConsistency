package com.mercadolibre.refunds_consistency.service;

import com.mercadolibre.refunds_consistency.constants.HeadersNames;
import com.mercadolibre.refunds_consistency.constants.UrlRequest;
import com.mercadolibre.refunds_consistency.model.PayinResponse;
import com.mercadolibre.refunds_consistency.model.Payment;
import com.mercadolibre.refunds_consistency.utils.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PayinService {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ConnectionService connectionService;

    /**
     * Realiza request para API de Payin
     * @param payment
     * @return PayinResponse
     */
    public PayinResponse checkPaymentInPayin(Payment payment) {
        String bankTransferId = paymentService.getBankTransferId(payment);

        if(bankTransferId == null){
            return null;
        }

        String uriRequestPayinAPI = this.buildPayinAPIUri(bankTransferId);
        ResponseEntity responseRequest = connectionService.doRequestApi(uriRequestPayinAPI, HttpMethod.GET);

        if(responseRequest != null){
            String responseBodyJSON = (String) responseRequest.getBody();
            return (PayinResponse) Parser.unmarshal(responseBodyJSON, new PayinResponse());
        }
        return null;
    }

    /**
     * Monta URI de chamada da API de Payin
     * @param bankTransferId
     * @return String
     */
    public String buildPayinAPIUri(String bankTransferId) {
        return UrlRequest.URL_INTERNAL_MP + UrlRequest.ENDPOINT_PAYIN + bankTransferId;
    }
}
