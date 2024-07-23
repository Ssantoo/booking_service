package com.example.booking.support;

import com.example.booking.controller.concert.ConcertController;
import com.example.booking.controller.concert.dto.ReservationRequest;
import com.example.booking.controller.concert.dto.ReservationResponse;
import com.example.booking.domain.concert.*;
import com.example.booking.domain.queue.QueueService;
import com.example.booking.domain.queue.Token;
import com.example.booking.infra.concert.entity.ReservationStatus;
import com.example.booking.infra.concert.entity.SeatStatus;
import com.example.booking.infra.token.entity.TokenStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@WebMvcTest(ConcertController.class)
@ExtendWith(MockitoExtension.class)
public class InterceptorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QueueService queueService;

    @MockBean
    private ConcertService concertService;

    @Autowired
    private WebApplicationContext context;

    private Token activeToken;

    private Token expiredToken;

    private  ReservationRequest request;

    private Seat mockSeat;

    private Reservation reservation;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new OncePerRequestFilter() {
                    @Override
                    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                        filterChain.doFilter(request, response);
                    }
                }).build();

        activeToken = Token.builder()
                .id(1L)
                .userId(1L)
                .token(UUID.randomUUID().toString())
                .expiredAt(LocalDateTime.now().plusHours(1))
                .status(TokenStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        expiredToken = Token.builder()
                .id(2L)
                .userId(1L)
                .token(UUID.randomUUID().toString())
                .expiredAt(LocalDateTime.now().minusHours(1))
                .status(TokenStatus.EXPIRED)
                .createdAt(LocalDateTime.now())
                .build();

        Concert concert = Concert.builder()
                .id(1L)
                .name("해리포터 1")
                .information("마법사의돌")
                .build();

        Schedule mockSchedule = Schedule.builder()
                .id(1L)
                .dateTime(LocalDateTime.now())
                .totalSeats(100)
                .availableSeats(50)
                .concert(concert)
                .build();

        mockSeat = Seat.builder()
                .id(1L)
                .seatNumber(1)
                .price(100)
                .status(SeatStatus.AVAILABLE)
                .schedule(mockSchedule)
                .build();
    }

    @Test
    public void 유효한_토큰일_때_정상_응답을_반환() throws Exception {
        given(queueService.findToken(anyString())).willReturn(Optional.of(activeToken));

        Reservation reservation = Reservation.builder()
                .id(1L)
                .concertScheduleId(1L)
                .seat(mockSeat)
                .userId(1L)
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalPrice(100)
                .build();

        ReservationResponse response = ReservationResponse.from(reservation);

        given(concertService.reserve(any(Reservation.class), anyString())).willReturn(reservation);

        ReservationRequest request = new ReservationRequest(activeToken.getToken(), 1L, mockSeat, 1L);

        mockMvc.perform(post("/api/concert/reserve-seat")
                        .header("Authorization", activeToken.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void 유효하지_않은_토큰일_때_401_응답을_반환() throws Exception {
        given(queueService.findToken(anyString())).willReturn(Optional.empty());

        mockMvc.perform(post("/api/concert/reserve-seat")
                        .header("Authorization", "만료됨")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void 토큰이_없을_때_401_응답을_반환() throws Exception {
        mockMvc.perform(post("/api/concert/reserve-seat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void 만료된_토큰일_때_401_응답을_반환() throws Exception {
        given(queueService.findToken(anyString())).willReturn(Optional.of(expiredToken));

        mockMvc.perform(post("/api/concert/reserve-seat")
                        .header("Authorization", expiredToken.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    //당연한건데.... 굳이..?
    @Test
    public void 인터셉터와_상관없는_API_요청() throws Exception {
        List<Concert> concerts = List.of(
                Concert.builder().id(1L).name("콘서트1").information("정보1").build(),
                Concert.builder().id(2L).name("콘서트2").information("정보2").build()
        );

        given(concertService.getConcertList()).willReturn(concerts);

        mockMvc.perform(get("/api/concert")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(concerts)));
    }
}
