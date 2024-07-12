package com.example.booking.controller.queue;

import com.example.booking.controller.queue.dto.TokenResponse;
import com.example.booking.domain.queue.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "토큰")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/queue")
public class QueueController {

    private final TokenService tokenService;

    /** 토큰
     * **유저 대기열 토큰 기능
     * - 서비스를 이용할 토큰을 발급받는 API를 작성합니다.
     * - 토큰은 유저의 UUID 와 해당 유저의 대기열을 관리할 수 있는 정보 ( 대기 순서 or 잔여 시간 등 ) 를 포함합니다.
     * - 이후 모든 API 는 위 토큰을 이용해 대기열 검증을 통과해야 이용 가능합니다.
     *
     */
    @Operation(summary = "토큰 생성")
    @PostMapping("/generate")
    public ResponseEntity<TokenResponse> generateToken(@RequestParam Long userId) {
        TokenResponse response = TokenResponse.from(tokenService.generate(userId));
        return ResponseEntity.ok(response);
    }


}