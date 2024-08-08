package com.example.booking.domain.event;

import com.example.booking.domain.user.User;
import org.springframework.context.ApplicationEvent;

public class PointRestoreEvent extends ApplicationEvent {

    private final User user;
    private final int amount;

    public PointRestoreEvent(Object source, User user, int amount) {
        super(source);
        this.user = user;
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public int getAmount() {
        return amount;
    }
}
