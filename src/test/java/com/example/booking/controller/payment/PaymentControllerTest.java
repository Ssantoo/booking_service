package com.example.booking.controller.payment;

import com.example.booking.application.PaymentFacade;
import com.example.booking.controller.payment.dto.PaymentResponse;
import com.example.booking.controller.user.UserController;
import com.example.booking.domain.concert.Concert;
import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.concert.Schedule;
import com.example.booking.domain.concert.Seat;
import com.example.booking.domain.payment.Payment;
import com.example.booking.domain.payment.PaymentStatus;
import com.example.booking.domain.user.User;
import com.example.booking.domain.user.UserService;
import com.example.booking.infra.concert.entity.ReservationStatus;
import com.example.booking.infra.concert.entity.SeatStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc
@WebMvcTest(PaymentController.class)
@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentFacade paymentFacade;

    @Test
    public void 콘서트_예약_결제() throws Exception {
        Long userId = 1L;
        Long reservationId = 1L;
        Schedule mockSchedule = Schedule.builder().id(1L).dateTime(LocalDateTime.now()).totalSeats(100).availableSeats(50).concert(new Concert(1L, "해리포터 1", "마법사의돌")).build();
        Seat mockSeat = Seat.builder()
                .id(1L)
                .seatNumber(1)
                .price(100)
                .status(SeatStatus.AVAILABLE)
                .schedule(mockSchedule)
                .build();

        Payment mockPayment = Payment.builder()
                .userId(new User(userId, "조현재", 1000))
                .reservationId(new Reservation(reservationId, 1L, 1L, mockSeat, ReservationStatus.PENDING, LocalDateTime.now(), LocalDateTime.now(), 100))
                .amount(100)
                .paymentTime(LocalDateTime.now())
                .status(PaymentStatus.CONFIRMED)
                .build();

        PaymentResponse mockResponse = PaymentResponse.from(mockPayment);
        String expectedJson = objectMapper.writeValueAsString(mockResponse);

        given(paymentFacade.processPayment(userId, reservationId)).willReturn(mockPayment);

        mockMvc.perform(post("/api/payments/pay")
                        .param("userId", userId.toString())
                        .param("reservationId", reservationId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String actualJson = result.getResponse().getContentAsString();
                    assertEquals(expectedJson, actualJson);
                });
    }
}
