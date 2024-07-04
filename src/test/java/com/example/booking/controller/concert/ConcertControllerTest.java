package com.example.booking.controller.concert;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ConcertController.class)
@AutoConfigureMockMvc
public class ConcertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 예약 가능 날짜 테스트
    @Test
    public void 예약_가능_날짜_조회() throws Exception {
        mockMvc.perform(get("/api/concert/{concertId}/available-dates", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("2024-07-05"))
                .andExpect(jsonPath("$[1]").value("2024-07-06"))
                .andExpect(jsonPath("$[2]").value("2024-07-07"));
    }

    // 예약 가능 좌석 테스트
    @Test
    public void 예약_가능_좌석_조회() throws Exception {
        mockMvc.perform(get("/api/concert/{concertId}/available-seats", "1")
                        .param("date", "2024-07-05")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(1))
                .andExpect(jsonPath("$[1]").value(2))
                .andExpect(jsonPath("$[2]").value(3))
                .andExpect(jsonPath("$[3]").value(4))
                .andExpect(jsonPath("$[4]").value(5));
    }
}
