package com.example.booking.controller.user;


import com.example.booking.controller.user.dto.ChargeRequest;
import com.example.booking.controller.user.dto.PointResponse;
import com.example.booking.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    /**
     * 잔액 충전 / 조회
     * - 결제에 사용될 금액을 API 를 통해 충전하는 API 를 작성합니다.
     * - 사용자 식별자 및 충전할 금액을 받아 잔액을 충전합니다.
     * - 사용자 식별자를 통해 해당 사용자의 잔액을 조회합니다.
     */

    //조회
    @Operation(summary = "잔액 조회")
    @GetMapping("/points")
    public ResponseEntity<PointResponse> getPoints(@RequestParam long userId) {
        PointResponse response = PointResponse.from(userService.getUserPoint(userId));
        return ResponseEntity.ok(response);
    }

    //충전
    @Operation(summary = "잔액 충전")
    @PostMapping("/charge")
    public ResponseEntity<PointResponse> chargePoints(@RequestBody ChargeRequest chargeRequest) {
        PointResponse response = PointResponse.from(userService.chargePoint(chargeRequest.getUserId(), chargeRequest.getAmount()));
        return ResponseEntity.ok(response);
    }










    /**
     * 유저 구매 내역 조회
     */
//    @GetMapping("/{userId}/payment-history")
//    public ResponseEntity<List<PaymentHistoryResponse>> getPaymentHistory(@PathVariable long userId) {
//        List<PaymentHistoryResponse> paymentHistory = Arrays.asList(
//                new PaymentHistoryResponse(userId, 1, "2024-07-05", "Concert A", 100),
//                new PaymentHistoryResponse(userId, 2, "2024-07-06", "Concert B", 150)
//        );
//        return ResponseEntity.ok(paymentHistory);
//    }

}
