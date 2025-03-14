package com.jerocaller.TestStudy.controller;

import com.jerocaller.TestStudy.business.UserService;
import com.jerocaller.TestStudy.common.ResponseMessages;
import com.jerocaller.TestStudy.data.entity.SiteUsers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 컨트롤러 계층에 속한 Bean의 테스트
 *
 * <p>
 *     참고자료: 장정우, “스프링 부트 핵심 가이드 - 스프링 부트를 활용한 애플리케이션 개발 실무”,
 *     (위키북스, 2024)
 * </p>
 *
 * <p>어노테이션 설명</p>
 * <p>
 *     <code>@WebMvcTest</code>
 *     웹에서 사용되는 요청 및 응답에 대한 테스트 수행에 사용됨.
 *     대상 클래스만 load하여 테스트를 수행한다. 만약 아무런 대상 클래스를 추가하지 않으면,
 *     <code>@Controller</code>, <code>@RestController</code>,
 *     <code>@ControllerAdvice</code> 등의 컨트롤러 관련 Bean 객체 모두 load됨.
 *     <code>@SpringBootTest</code>보다 가볍게 테스트할 때 사용.
 * </p>
 * <p>
 *     <code>@MockitoBean</code>
 *     원래는 <code>@MockBean</code>이었으나 현재 deprecated되어 대신 사용되고 있는
 *     어노테이션으로, 실제 Bean 객체가 아닌 Mock(가짜) 객체를 생성, 주입한다.
 *     실제 객체가 아니므로 실제 메인 애플리케이션 쪽에 작성한대로 수행되지 않음.
 *     따라서 Mockito 제공 given() 메서드를 통해 그 동작을 정의해야 함.
 *     테스트 대상 클래스에 실제 주입되는 외부 의존성으로부터 독립적인 테스트를 하고자 할 때
 *     사용.
 * </p>
 */
@WebMvcTest(UserController.class)
public class UserControllerTest2 {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @DisplayName("MockMvc를 이용하여 사용자 데이터 조회 테스트")
    @Test
    public void userTest() throws Exception {

        final Integer targetId = 100;

        given(userService.getOneUserById(100)).willReturn(
            SiteUsers.builder()
                .memberId(targetId)
                .signUpDate(LocalDate.now())
                .mileage(1000)
                .username("kimquel")
                .averPurchase(123.40)
                .build()
        );

        final String url = "/api/users/{id}";

        mockMvc.perform(get(url, targetId))  // "/api/users/100"
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message")
                .value(ResponseMessages.READ_OK)
            )
            .andExpect(jsonPath("$.data.memberId").value(targetId))
            .andExpect(jsonPath("$.data.username").value("kimquel"))
            .andExpect(jsonPath("$.data.recommBy").doesNotExist())
            .andDo(print());  // 이 테스트에서 사용된 mock Http 요청 및 응답 세부 내용 출력

        // 지정된 메서드의 실행 여부를 검증.
        // 여기서는 given()에 정의된 동작이 실제로 실행되었는지 여부를 검증한다. 
        verify(userService).getOneUserById(100);
        
    }

}
