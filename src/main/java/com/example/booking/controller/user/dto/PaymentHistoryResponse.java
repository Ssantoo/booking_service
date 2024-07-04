package com.example.booking.controller.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentHistoryResponse {

    private long userId;
    private int seatNumber;
    private String date;
    private String concertName;
    private int amount;

    public PaymentHistoryResponse(long userId, int seatNumber, String date, String concertName, int amount) {
        this.userId = userId;
        this.seatNumber = seatNumber;
        this.date = date;
        this.concertName = concertName;
        this.amount = amount;
    }

}
