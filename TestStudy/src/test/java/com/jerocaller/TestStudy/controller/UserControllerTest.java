package com.jerocaller.TestStudy.controller;

import com.jerocaller.TestStudy.data.entity.ClassType;
import com.jerocaller.TestStudy.data.entity.SiteUsers;
import com.jerocaller.TestStudy.data.repository.ClassTypeRepository;
import com.jerocaller.TestStudy.data.repository.SiteUsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * <p>
 *     참고 자료) 신선영, “스프링 부트 3 백엔드 개발자 되기 - 자바 편, 2판”, (골든래빗)
 * </p>
 *
 * <p>
 *     메인 패키지에서 제작한 컨트롤러를 테스트하기 위한 코드
 * </p>
 * 
 * <p>에너테이션 설명</p>
 * <p>
 * <code>@SpringBootTest</code> : 메인 애플리케이션 클래스에 부여된
 * <code>@SpringBootApplication</code> 애너테이션을 통해 해당 클래스를 검색하고,
 * 해당 클래스에 포함된 bean 조회 후 이를 토대로 테스트용 애플리케이션 컨텍스트를 생성함. 
 * </p>
 *
 * <p>
 *     <code>@AutoConfigureMockMvc</code> : MockMvc 객체를 자동 생성 및 구성.
 *     MockMvc는 애플리케이션을 실제 서버로 배포하지 않아도 테스트용 MVC 환경을 구성하여
 *     요청, 응답 전송 기능을 제공하는 유틸리티 클래스. 주로 웹과 가장 가까운 영역인
 *     컨트롤러 계층의 클래스를 테스트할 때 사용됨.
 * </p>
 * 
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SiteUsersRepository siteUsersRepository;

    @Autowired
    private ClassTypeRepository classTypeRepository;

    // DB에 저장된 테스트용 데이터들을 기억했다가 삭제하기 위한 용도.
    private final List<SiteUsers> usersForTest = new ArrayList<>();
    private final List<ClassType> classTypesForTest = new ArrayList<>();

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .build();
    }

    @AfterEach
    public void clean() {
        // 테스트 케이스에 사용된 테스트용 데이터들을 DB로부터 삭제한다.
        usersForTest.forEach(siteUsersRepository::delete);
        classTypesForTest.forEach(classTypeRepository::delete);

        // 기록용 리스트들의 내부를 비운다.
        usersForTest.clear();
        classTypesForTest.clear();
    }

    @DisplayName("getAllUsers : 모든 사용자 조회에 성공한다.")
    @Test
    public void getAllUsers() throws Exception {

        // Given
        // 테스트용 URL 및 사용자 객체를 생성한 후, 해당 사용자 객체를 DB에 저장.
        final String url = "/api/users";

        ClassType classType = classTypeRepository.findById(1)
            .orElseThrow(() -> new EntityNotFoundException("해당 타입 미존재"));

        SiteUsers newUser = SiteUsers.builder()
            .memberId(100)
            .signUpDate(LocalDate.now())
            .mileage(100)
            .username("kimquel")
            .averPurchase(120.5)
            .classType(classType)
            .build();
        SiteUsers savedUser = siteUsersRepository.save(newUser);
        usersForTest.add(savedUser);

        // When
        // 주어진 URL을 통해 API 호출하여 회원 조회 시도.
        // mockMvc.perform : 요청을 전송하는 역할.
        // 이 결과로 ResultActions 객체가 반환되는데, 이 객체는 이후
        // 결과값 검증 및 확인용의 andExpect() 메서드를 제공함.
        // 한 편, accept()는 응답 타입을 결정하는데에 사용되는 메서드로,
        // 여기서는 JSON 타입으로 받는 것으로 설정함.
        final ResultActions result = mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON));

        // Then
        // 응답 코드가 OK(200)이고,
        // JSON 형태의 결과값 중 마지막 요소의 id, username의 값이 앞서
        // 저장한 사용자 객체의 것과 같은지 비교.

        // status().isOk() : 응답의 HTTP Status가 OK (200)인지 검증.
        // isCreated() 등 여러 다른 Status를 위한 메서드들도 존재함.
        // jsonPath("$") : JSON 형태의 응답 값을 받아올 때 사용.
        // 여기서 $는 받은 JSON 값 자체를 의미하는 듯 하다.
        // $.data.[-1].memberId )
        // {
        //      "httpStatus": "OK",
        //      "message": ...,
        //      "data": {
        //          ...,
        //          { // 마지막 데이터 ([-1])
        //              "memberId": 100,  -> $.data.[-1].memberId
        //              "username": "kimquel"  -> $.data.[-1].username
        //          }
        //      }
        // }
        //
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.[-1].memberId")
                .value(savedUser.getMemberId())
            )
            .andExpect(jsonPath("$.data.[-1].username")
                .value(savedUser.getUsername()));
    }

}