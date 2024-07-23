package com.example.booking.support.exception;

public class AlreadyOccupiedException extends RuntimeException {
    public AlreadyOccupiedException(String message) {
        super(message);
    }
}
