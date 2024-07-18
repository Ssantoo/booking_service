package com.example.booking.support.interceptor;

import com.example.booking.domain.queue.QueueService;
import com.example.booking.domain.queue.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final QueueService queueService;

    public TokenInterceptor(QueueService queueService) {
        this.queueService = queueService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "header 체크");
            return false;
        }

        Optional<Token> tokenOpt = queueService.findToken(token);

        if (tokenOpt.isEmpty() || !tokenOpt.get().isActive()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료됨");
            return false;
        }

        return true;
    }
}
