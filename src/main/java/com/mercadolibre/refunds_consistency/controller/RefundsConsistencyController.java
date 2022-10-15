package com.mercadolibre.refunds_consistency.controller;

import com.mercadolibre.refunds_consistency.dto.PaymentDTO;
import com.mercadolibre.refunds_consistency.model.ResponseModel;
import com.mercadolibre.refunds_consistency.service.RefundConsistencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RefundsConsistencyController {

    @Autowired
    private RefundConsistencyService refundConsistencyService;

    @GetMapping("/check_consistency")
    private ResponseEntity<List<ResponseModel>> checkPaymentsRefundConsistency(
            @RequestHeader(name = "x-auth-token") String furyToken,
            @RequestHeader(name = "cookie") String cookieSessionToken,
            @RequestBody List<@Valid PaymentDTO> payments
    ) {
        List<ResponseModel> paymentsList = refundConsistencyService.proccessPayments(payments, furyToken, cookieSessionToken);
        return ResponseEntity.ok().body(paymentsList);
    }
}
