package com.mercadolibre.refunds_consistency.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.refunds_consistency.constants.FinalStatus;
import com.mercadolibre.refunds_consistency.constants.HeadersName;
import com.mercadolibre.refunds_consistency.constants.UrlRequest;
import com.mercadolibre.refunds_consistency.dto.PaymentDTO;
import com.mercadolibre.refunds_consistency.model.PayinResponse;
import com.mercadolibre.refunds_consistency.model.Payment;
import com.mercadolibre.refunds_consistency.model.PaymentResponse;
import com.mercadolibre.refunds_consistency.model.ResponseModel;
import lombok.Getter;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RefundConsistencyService {

    private RestTemplate restTemplate = new RestTemplate();
    private HttpMethod httpMethod;
    private HttpEntity<?> httpEntity;
    private ResponseEntity responseEntity;
    private Map<String, String> requestHeaders;
    @Getter
    private List<ResponseModel> responseRequestList;


    public List<ResponseModel> proccessPayments(List<PaymentDTO> payments, Map<String, String> requestHeaders){
        this.responseRequestList = new ArrayList<>();
        this.getClientHeaders(requestHeaders);
        this.setHttpMethod(HttpMethod.GET);


        for(PaymentDTO paymentDTO : payments) {
            HttpHeaders headers = this.setHeaders(HeadersName.FURY_HEADER, HeadersName.ONE_SOURCE_COOKIE_HEADER);
            this.httpEntity = new HttpEntity<>(headers);
            Long paymentId = paymentDTO.getPayment_id();
            String uriRequestOneSource = this.buildOneSourceUri(paymentId);
            this.doRequestApi(uriRequestOneSource);

            if(this.responseEntity == null){
                continue;
            }

            String responseBody = (String) this.responseEntity.getBody();
            PaymentResponse responsePayment = (PaymentResponse) parseResponseEntity(responseBody, new PaymentResponse());
            PayinResponse responsePayin = checkPaymentInPayin(responsePayment);
            this.mountResponseAnalisys(responsePayment, responsePayin);
        }
        return this.responseRequestList;
    }


    private void doRequestApi(String urlRequest) {
        try{
            this.responseEntity = this.restTemplate.exchange(urlRequest, this.httpMethod, this.httpEntity, String.class);
        }catch(RuntimeException e){
            this.responseEntity = null;
        }
    }

    private PayinResponse checkPaymentInPayin(PaymentResponse paymentResponse) {
        Payment payment = paymentResponse.getPayments().get(0);
        String bankTransferId = this.getBankTransferId(payment);

        if(bankTransferId == null){
            return null;
        }

        String uriRequestPayinAPI = this.buildPayinAPIUri(bankTransferId);

        HttpHeaders headers = this.setHeaders(HeadersName.FURY_HEADER);
        this.httpEntity = new HttpEntity<>(headers);
        setHttpMethod(HttpMethod.GET);
        this.doRequestApi(uriRequestPayinAPI);

        if(this.responseEntity == null){
            return new PayinResponse();
        }

        String responseBody = (String) this.responseEntity.getBody();
        return (PayinResponse) parseResponseEntity(responseBody, new PayinResponse());
    }


    private void getClientHeaders(Map<String, String> requestHeaders) {
        Map<String, String> toUpperCaseHeaders = new HashMap<>();
        requestHeaders.forEach((key, value) -> {
            toUpperCaseHeaders.put(key.toUpperCase(), value);
        });
        this.requestHeaders = toUpperCaseHeaders;
    }


    private HttpHeaders setHeaders(String... headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        for(String header : headers){
            httpHeaders.set(header, this.requestHeaders.get(header));
        }
        return httpHeaders;
    }


    private void mountResponseAnalisys(PaymentResponse paymentResponse, PayinResponse payinResponse) {
        for(Payment payment : paymentResponse.getPayments()){
            ResponseModel currentPayment =  buildResponseModel(payment, payinResponse);
            this.addItem(currentPayment);
        }
    }


    private void addItem(ResponseModel response) {
        this.responseRequestList.add(response);
    }


    private ResponseModel buildResponseModel(Payment payment, PayinResponse payinResponse) {

        StringBuilder contingencies = new StringBuilder();
        for (String contingencyItem : payment.getContingencies().getList()){
            contingencies.append(contingencyItem).append(";");
        }

        Integer qtdRefundsPayin = (payinResponse != null)
                ? payinResponse.getRefunds().size()
                : null;
        String lastRefundId = (qtdRefundsPayin != null)
                ? payinResponse.getRefunds().get(qtdRefundsPayin-1).getId()
                : null;

        String lastRefundStatus = (qtdRefundsPayin != null)
                ? payinResponse.getRefunds().get(qtdRefundsPayin-1).getStatus()
                : null;

        Integer qtdRefundsPayments = payment.getRefunds().size();
        String finalStatus = this.chooseFinalStatus(qtdRefundsPayin, qtdRefundsPayments, contingencies.toString());

        return ResponseModel.builder()
            .payment_id(payment.getId())
            .contingencies(contingencies.toString())
            .contingencie_status(payment.getContingencies().getStatus())
            .qtd_refunds_payments_api(qtdRefundsPayments)
            .bank_transfer_id(payment.getTransaction_details().getBank_transfer_id())
            .qtd_refunds_payin_api(qtdRefundsPayin)
            .last_refund_id(lastRefundId)
            .last_refund_status(lastRefundStatus)
            .final_status_payment(finalStatus).build();
    }

    private String chooseFinalStatus(Integer qtdRefundsPayin, Integer qtdRefundsPayments, String contingencies) {
        if(qtdRefundsPayin == null)
            return FinalStatus.PAGO_ROTO.getFinalStatus();
        if(!contingencies.equals(""))
            return FinalStatus.SOLVE_CONTINGENCY.getFinalStatus();
        if(!qtdRefundsPayin.equals(qtdRefundsPayments))
            return FinalStatus.SOLVED_INCONSISTENCY.getFinalStatus();
        return FinalStatus.READY_BPO_REFUND.getFinalStatus();
    }


    private String getBankTransferId(Payment payment) {
        return (payment.getTransaction_details().getBank_transfer_id() != null && payment.getTransaction_details().getBank_transfer_id() != 0)
                ? payment.getTransaction_details().getBank_transfer_id().toString()
                : null;
    }


    private String buildOneSourceUri(Long paymentId) {
        return UrlRequest.URL_ONE_SOURCE + UrlRequest.ENDPOINT_ONE_SOURCE + paymentId.toString();
    }


    private String buildPayinAPIUri(String bankTransferId) {
        return UrlRequest.URL_INTERNAL_MP + UrlRequest.ENDPOINT_PAYIN + bankTransferId;
    }


    private void setHttpMethod(HttpMethod method) {
        this.httpMethod = method;
    }


    private Object parseResponseEntity(String response, Object model) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response, model.getClass());
        }catch(IOException e){
            return e.getMessage();
        }
    }
}
