package com.jerocaller.TestStudy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuizControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void mockMvcSetUp() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .build();

    }

    @DisplayName("""
        quiz() : GET /quiz?code=1이면 응답 코드는 201, 응답 본문은 Created! 반환
    """)
    @Test
    public void quiz1() throws Exception {

        // Given
        final String url = "/quiz";
        final String targetCode = "1";

        // When
        final ResultActions result = mockMvc.perform(get(url)
            .param("code", targetCode)
            .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        result
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").value("Created!"));

    }

    @DisplayName("""
       quiz() : GET /quiz?code=2이면 응답 코드 400, 응답 본문은 Bad Request를 반환. 
    """)
    @Test
    public void quiz2() throws Exception {

        // Given
        final String url = "/quiz";
        final String targetCode = "2";

        // When
        final ResultActions result = mockMvc.perform(get(url)
            .param("code", targetCode)
        );

        // Then
        result.andExpect(status().isBadRequest())
            .andExpect(content().string("Bad Request!"));

    }

    @DisplayName("""
        quiz2() : POST /quiz?code=1 이면 응답 코드는 403, 
        응답 본문은 'Forbidden!'을 반환한다.
    """)
    @Test
    public void quiz3() throws Exception {

        // Given
        final String url = "/quiz";
        final Code targetCode = new Code();
        targetCode.setValue(1);

        // When
        final ResultActions result = mockMvc.perform(post(url)
            .content(objectMapper.writeValueAsString(targetCode))
            .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        result.andExpect(status().isForbidden())
            .andExpect(content().string("Forbidden!"));

    }

    @DisplayName("""
        quiz2() : POST /quiz?code=5 이면 응답 코드는 200,
        응답 본문은 'OK!'를 리턴한다.
    """)
    @Test
    public void quiz4() throws Exception {

        // Given
        final String url = "/quiz";
        final Code targetCode = new Code();
        targetCode.setValue(5);

        // When
        final ResultActions result = mockMvc.perform(post(url)
            .content(objectMapper.writeValueAsString(targetCode))
            .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        result.andExpect(status().isOk())
            .andExpect(content().string("OK!"));

    }

}