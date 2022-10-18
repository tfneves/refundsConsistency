package com.mercadolibre.refunds_consistency.service;

import com.mercadolibre.refunds_consistency.constants.UrlRequest;
import com.mercadolibre.refunds_consistency.model.Payment;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {


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
