package com.mercadolibre.refunds_consistency.service;

import com.mercadolibre.refunds_consistency.constants.HeadersNames;
import com.mercadolibre.refunds_consistency.constants.UrlRequest;
import com.mercadolibre.refunds_consistency.dto.PaymentDTO;
import com.mercadolibre.refunds_consistency.model.Payment;
import com.mercadolibre.refunds_consistency.model.PaymentResponse;
import com.mercadolibre.refunds_consistency.utils.Parser;
import com.mercadolibre.refunds_consistency.utils.ValidateAuthorizationHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private ConnectionService connectionService;


    public PaymentResponse checkPaymentInOneSource(PaymentDTO paymentDTO) {
        String uri = this.buildOneSourceUri(paymentDTO.getPayment_id());
        ResponseEntity responseRequest = connectionService.doRequestApi(uri, HttpMethod.GET, HeadersNames.FURY_HEADER.getHeaderName(), HeadersNames.ONE_SOURCE_COOKIE_HEADER.getHeaderName());

        if(responseRequest != null){
            String responseBodyJSON = (String) responseRequest.getBody();
            return (PaymentResponse) Parser.unmarshal(responseBodyJSON, new PaymentResponse());
        }
        return null;
    }

    /**
     * Monta URI de chamada da API de Payments - One Source
     * @param paymentId
     * @return String
     */
    public String buildOneSourceUri(Long paymentId) {
        return UrlRequest.URL_ONE_SOURCE + UrlRequest.ENDPOINT_ONE_SOURCE + paymentId.toString();
    }

    /**
     * Pega o valor do atributo bankTransferId
     * @param payment
     * @return String
     */
    public String getBankTransferId(Payment payment) {
        return (payment.getTransaction_details().getBank_transfer_id() != null && payment.getTransaction_details().getBank_transfer_id() != 0)
                ? payment.getTransaction_details().getBank_transfer_id().toString()
                : null;
    }

    /**
     * Monta e retorna uma string com a lista de contingencias de um pagamento
     * @param payment
     * @return String
     */
    public String mountContingenciesList(Payment payment) {
        StringBuilder contingenciesList = new StringBuilder();
        for (String contingencyItem : payment.getContingencies().getList()){
            contingenciesList.append(contingencyItem).append(";");
        }
        return contingenciesList.toString();
    }

    /**
     * Pega e retorna o total de Refunds do pagamento na API de Payments
     * @param payment
     * @return Integer
     */
    public Integer getTotalRefunds(Payment payment) {
        return payment.getRefunds().size();
    }
}
