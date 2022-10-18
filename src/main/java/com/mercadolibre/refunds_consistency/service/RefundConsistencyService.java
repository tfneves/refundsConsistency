package com.mercadolibre.refunds_consistency.service;

import com.mercadolibre.refunds_consistency.constants.FinalStatus;
import com.mercadolibre.refunds_consistency.constants.HeadersName;
import com.mercadolibre.refunds_consistency.dto.PaymentDTO;
import com.mercadolibre.refunds_consistency.model.PayinResponse;
import com.mercadolibre.refunds_consistency.model.Payment;
import com.mercadolibre.refunds_consistency.model.PaymentResponse;
import com.mercadolibre.refunds_consistency.model.ResponseModel;
import com.mercadolibre.refunds_consistency.utils.Parser;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RefundConsistencyService {


    @Getter
    private List<ResponseModel> responseRequestList;

    @Autowired
    private ConnectionService connectionService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PayinService payinService;


    /**
     * Recebe dados do controller e inicia processo de verificacao dos pagamentos
     * @param payments
     * @param requestHeaders
     * @return ResponseModel
     */
    public List<ResponseModel> checkPaymentStatus(List<PaymentDTO> payments, Map<String, String> requestHeaders){
        this.responseRequestList = new ArrayList<>();
        connectionService.getClientHeaders(requestHeaders);

        for(PaymentDTO paymentDTO : payments) {
            String uri = paymentService.buildOneSourceUri(paymentDTO.getPayment_id());
            ResponseEntity responseRequest = connectionService.doRequestApi(uri, HttpMethod.GET, HeadersName.FURY_HEADER, HeadersName.ONE_SOURCE_COOKIE_HEADER);

            if(responseRequest == null){
                continue;
            }

            String responseBodyJSON = (String) responseRequest.getBody();
            PaymentResponse responsePayment = (PaymentResponse) Parser.unmarshal(responseBodyJSON, new PaymentResponse());
            PayinResponse responsePayin = payinService.checkPaymentInPayin(responsePayment);
            this.mountResponseAnalisys(responsePayment, responsePayin);
        }
        return this.responseRequestList;
    }


    private void mountResponseAnalisys(PaymentResponse paymentResponse, PayinResponse payinResponse) {
        for(Payment payment : paymentResponse.getPayments()){
            ResponseModel currentPayment =  buildResponseModel(payment, payinResponse);
            this.responseRequestList.add(currentPayment);
        }
    }


    /**
     * Monta e retorna o model final do pagamento analisado
     * @param payment
     * @param payinResponse
     * @return ResponseModel
     */
    private ResponseModel buildResponseModel(Payment payment, PayinResponse payinResponse) {

        String contingenciesList = paymentService.mountContingenciesList(payment);
        Integer qtdRefundsPayments = paymentService.getTotalRefunds(payment);

        Integer qtdRefundsPayin = (payinResponse != null)
                ? payinResponse.getRefunds().size()
                : null;
        String lastRefundId = (qtdRefundsPayin != null)
                ? payinResponse.getRefunds().get(qtdRefundsPayin-1).getId()
                : null;

        String lastRefundStatus = (qtdRefundsPayin != null)
                ? payinResponse.getRefunds().get(qtdRefundsPayin-1).getStatus()
                : null;

        String finalStatus = this.chooseFinalStatus(qtdRefundsPayin, qtdRefundsPayments, contingenciesList);

        return ResponseModel.builder()
            .payment_id(payment.getId())
            .contingencies(contingenciesList)
            .contingencie_status(payment.getContingencies().getStatus())
            .qtd_refunds_payments_api(qtdRefundsPayments)
            .bank_transfer_id(payment.getTransaction_details().getBank_transfer_id())
            .qtd_refunds_payin_api(qtdRefundsPayin)
            .last_refund_id(lastRefundId)
            .last_refund_status(lastRefundStatus)
            .final_status_payment(finalStatus).build();
    }


    /**
     * Define o status final do pagamento após a
     * analise na API de Payments e na API de Payin
     * @param qtdRefundsPayin
     * @param qtdRefundsPayments
     * @param contingencies
     * @return String
     */
    private String chooseFinalStatus(Integer qtdRefundsPayin, Integer qtdRefundsPayments, String contingencies) {
        if(qtdRefundsPayin == null)
            return FinalStatus.PAGO_ROTO.getFinalStatus();
        if(!contingencies.equals(""))
            return FinalStatus.SOLVE_CONTINGENCY.getFinalStatus();
        if(!qtdRefundsPayin.equals(qtdRefundsPayments))
            return FinalStatus.SOLVED_INCONSISTENCY.getFinalStatus();
        return FinalStatus.READY_BPO_REFUND.getFinalStatus();
    }
}
