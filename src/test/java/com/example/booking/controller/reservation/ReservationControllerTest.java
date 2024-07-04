package com.example.booking.controller.reservation;

import com.example.booking.controller.reservation.dto.ReservationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //좌석 예약
    @Test
    public void 좌석_예약_요청() throws Exception {
        LocalDateTime date = LocalDateTime.of(2024, 7, 5, 0, 0);
        ReservationRequest request = new ReservationRequest(1L,date, 1);

        mockMvc.perform(post("/api/reservation/reserve-seat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("reserved"))
                .andExpect(jsonPath("$.seatNumber").value(1))
                .andExpect(jsonPath("$.holdTime").value(5));
    }
}
