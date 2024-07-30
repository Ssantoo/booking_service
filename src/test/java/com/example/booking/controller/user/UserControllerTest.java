package com.example.booking.controller.user;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.example.booking.controller.user.dto.ChargeRequest;
import com.example.booking.controller.user.dto.PointResponse;
import com.example.booking.domain.user.User;
import com.example.booking.domain.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    //유저포인트 조회
    @Test
    public void 유저_포인트_조회() throws Exception {
        long userId = 1L;
        User mockUser = new User(userId, "조현재", 1000, 0);

        given(userService.getUserPoint(userId)).willReturn(mockUser);

        PointResponse expectedResponse = PointResponse.from(mockUser);
        String expectedJson = objectMapper.writeValueAsString(expectedResponse);


        mockMvc.perform(get("/api/user/points")
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expectedJson, result.getResponse().getContentAsString()));
    }

    //유저포인트 충전
    @Test
    public void 유저_포인트_충전() throws Exception {
        ChargeRequest chargeRequest = new ChargeRequest(1L, 500);
        User mockUser = User.builder()
                .id(1L)
                .name("조현재")
                .point(1500)
                .build();

        given(userService.chargePoint(chargeRequest.getUserId(), chargeRequest.getAmount())).willReturn(mockUser);

        PointResponse expectedResponse = PointResponse.from(mockUser);
        String expectedJson = objectMapper.writeValueAsString(expectedResponse);

        mockMvc.perform(post("/api/user/charge")
                        .content(objectMapper.writeValueAsString(chargeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expectedJson, result.getResponse().getContentAsString()));
    }


}
