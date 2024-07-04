package com.example.booking.controller.user;


import com.example.booking.controller.user.dto.ChargeRequest;
import com.example.booking.controller.user.dto.PointResponse;
import com.example.booking.controller.user.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    /** 토큰
     * **유저 대기열 토큰 기능
     * - 서비스를 이용할 토큰을 발급받는 API를 작성합니다.
     * - 토큰은 유저의 UUID 와 해당 유저의 대기열을 관리할 수 있는 정보 ( 대기 순서 or 잔여 시간 등 ) 를 포함합니다.
     * - 이후 모든 API 는 위 토큰을 이용해 대기열 검증을 통과해야 이용 가능합니다.
     *
     */
    @PostMapping("/token")
    public ResponseEntity<TokenResponse> generateToken() {
        TokenResponse response = new TokenResponse("exampleTokenTest", 1, 300);
        return ResponseEntity.ok(response);
    }

    /**
     * 잔액 충전 / 조회
     * - 결제에 사용될 금액을 API 를 통해 충전하는 API 를 작성합니다.
     * - 사용자 식별자 및 충전할 금액을 받아 잔액을 충전합니다.
     * - 사용자 식별자를 통해 해당 사용자의 잔액을 조회합니다.
     */
    @GetMapping("/points")
    public ResponseEntity<PointResponse> getPoints(@RequestParam long userId) {
        PointResponse response = new PointResponse(userId, 1000);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/charge")
    public ResponseEntity<PointResponse> chargePoints(@RequestBody ChargeRequest chargeRequest) {
        PointResponse response = new PointResponse(chargeRequest.getUserId(), 1000);
        return ResponseEntity.ok(response);
    }

}
