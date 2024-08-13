package com.example.booking.application.listener;

import com.example.booking.domain.event.PointRestoreEvent;
import com.example.booking.domain.user.User;
import com.example.booking.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointRestoreEventListener {

    private final UserService userService;

    @EventListener
    public void handlePointRestoreEvent(PointRestoreEvent event) {
        User user = event.getUser();
        int amount = event.getAmount();
        userService.restorePoints(user, amount);
    }
}
