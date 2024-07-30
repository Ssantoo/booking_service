package com.example.booking.controller.payment;

import com.example.booking.application.PaymentFacade;
import com.example.booking.controller.payment.dto.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "결제")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentFacade paymentFacade;

//    @Operation(summary = "콘서트 예약 결제")
//    @PostMapping("/pay")
//    public ResponseEntity<PaymentResponse> pay(@RequestParam Long userId, @RequestParam Long reservationId) {
//        PaymentResponse response = PaymentResponse.from(paymentFacade.processPayment(userId, reservationId));
//        return ResponseEntity.ok(response);
//    }
}
