package com.example.booking.controller.concert;

import com.example.booking.controller.concert.dto.ScheduleResponse;
import com.example.booking.controller.concert.dto.SeatResponse;
import com.example.booking.domain.concert.Concert;
import com.example.booking.domain.concert.ConcertService;
import com.example.booking.domain.concert.Schedule;
import com.example.booking.domain.concert.Seat;
import com.example.booking.infra.concert.entity.SeatStatus;
import com.example.booking.support.exception.AlreadyOccupiedException;
import com.example.booking.support.exception.CustomException;
import com.example.booking.support.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConcertController.class)
@AutoConfigureMockMvc
public class ConcertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ConcertService concertService;
    @Test
    public void 콘서트_목록_조회() throws Exception {
        List<Concert> mockConcertList = List.of(
                new Concert(1L, "harry 1", "test1"),
                new Concert(2L, "potter 2", "test2")
        );
        given(concertService.getConcertList()).willReturn(mockConcertList);

        String expectedJson = objectMapper.writeValueAsString(mockConcertList);

        mockMvc.perform(get("/api/concert")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expectedJson, result.getResponse().getContentAsString()));
    }

    // 예약 가능 날짜 테스트
    @Test
    public void 예약_가능_날짜_조회() throws Exception {
        long concertId = 1L;

        List<Schedule> mockScheduleList = List.of(
                new Schedule(1L, LocalDateTime.of(2024, 7, 5, 0, 0), 100, 50, new Concert(concertId, "harry 1", "potter1")),
                new Schedule(2L, LocalDateTime.of(2024, 7, 6, 0, 0), 100, 50, new Concert(concertId, "harry 2", "potter2")),
                new Schedule(3L, LocalDateTime.of(2024, 7, 7, 0, 0), 100, 50, new Concert(concertId, "harry 3", "potter3"))
        );

        given(concertService.getDates(concertId)).willReturn(mockScheduleList);

        List<ScheduleResponse> expectedScheduleList = ScheduleResponse.from(mockScheduleList);
        String expectedJson = objectMapper.writeValueAsString(expectedScheduleList);

        mockMvc.perform(get("/api/concert/{concertId}/available-dates", concertId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String actualJson = result.getResponse().getContentAsString();
                    assertEquals(expectedJson, actualJson);
                });
    }

//    @Test
//    public void 예약_좌석_조회() throws Exception {
//        long scheduleId = 1L;
//        long concertId = 1L;
//        Schedule schedule = new Schedule(1L, LocalDateTime.of(2024, 7, 5, 0, 0), 100, 50, new Concert(concertId, "harry 1", "potter"));
//        List<Seat> mockSeatList = List.of(
//                new Seat(1L, 1, 1000, SeatStatus.AVAILABLE, schedule),
//                new Seat(2L, 2,1000, SeatStatus.AVAILABLE, schedule)
//        );
//
//        given(concertService.getAvailableSeats(scheduleId)).willReturn(mockSeatList);
//
//        List<SeatResponse> expectedSeatList = SeatResponse.from(mockSeatList);
//        String expectedJson = objectMapper.writeValueAsString(expectedSeatList);
//
//        mockMvc.perform(get("/api/concert/{scheduleId}/available-seats", scheduleId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(result -> {
//                    String actualJson = result.getResponse().getContentAsString();
//                    assertEquals(expectedJson, actualJson);
//                });
//    }



    @Test
    public void 콘서트_목록_조회_리소스_예외() throws Exception {
        given(concertService.getConcertList()).willThrow(new ResourceNotFoundException("콘서트를 찾을 수 없습니다"));

        mockMvc.perform(get("/api/concert")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"status\":\"NOT_FOUND\",\"message\":\"콘서트를 찾을 수 없습니다\"}"));
    }

    @Test
    public void 예약_가능_날짜_조회_예외() throws Exception {
        long concertId = 1L;

        given(concertService.getDates(concertId)).willThrow(new CustomException("잘못된 요청입니다.", HttpStatus.BAD_REQUEST));

        mockMvc.perform(get("/api/concert/{concertId}/available-dates", concertId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"status\":\"BAD_REQUEST\",\"message\":\"잘못된 요청입니다.\"}"));
    }

    @Test
    public void 예약_좌석_조회_예외() throws Exception {
        long scheduleId = 1L;

        given(concertService.getAvailableSeats(scheduleId)).willThrow(new RuntimeException("에러가 발생했습니다."));

        mockMvc.perform(get("/api/concert/{scheduleId}/available-seats", scheduleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"status\":\"INTERNAL_SERVER_ERROR\",\"message\":\"에러가 발생했습니다.\"}"));
    }

    @Test
    public void 예약_좌석_조회_충돌_예외() throws Exception {
        long scheduleId = 1L;

        given(concertService.getAvailableSeats(scheduleId)).willThrow(new AlreadyOccupiedException("이미 예약된 좌석입니다."));

        mockMvc.perform(get("/api/concert/{scheduleId}/available-seats", scheduleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"status\":\"CONFLICT\",\"message\":\"이미 예약된 좌석입니다.\"}"));
    }
}
