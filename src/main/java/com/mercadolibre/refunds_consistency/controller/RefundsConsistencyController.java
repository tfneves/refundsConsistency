package com.mercadolibre.refunds_consistency.controller;

import com.mercadolibre.refunds_consistency.dto.PaymentDTO;
import com.mercadolibre.refunds_consistency.model.ResponseModel;
import com.mercadolibre.refunds_consistency.service.RefundConsistencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class RefundsConsistencyController {

    @Autowired
    private RefundConsistencyService refundConsistencyService;

    @GetMapping("/ping")
    private ResponseEntity<?> ping() {
        return ResponseEntity.ok().body("pong");
    }

    @GetMapping("/check_consistency")
    private ResponseEntity<List<ResponseModel>> checkPaymentsRefundConsistency(
            @RequestHeader Map<String, String> headers,
            @RequestBody List<@Valid PaymentDTO> payments
    ) {
        List<ResponseModel> paymentsList = refundConsistencyService.checkPaymentStatus(payments, headers);
        return ResponseEntity.ok().body(paymentsList);
    }
}
