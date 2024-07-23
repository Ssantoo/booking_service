package com.example.booking.support.exception;

public class NotReservableException extends RuntimeException {
    public NotReservableException(String message) {
        super(message);
    }
}