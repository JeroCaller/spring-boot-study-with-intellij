package com.jerocaller.TestStudy.business;

import com.jerocaller.TestStudy.data.entity.ClassType;
import com.jerocaller.TestStudy.data.entity.SiteUsers;
import com.jerocaller.TestStudy.data.repository.ClassTypeRepository;
import com.jerocaller.TestStudy.data.repository.SiteUsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

/**
 *
 * <p>
 *     <code>@ExtendWith(SpringExtension.class)</code> -
 *     스프링에서 객체를 주입받기 위한 어노테이션.
 *     SpringExtension 클래스는 JUnit의 Jupiter 테스트에 스프링 테스트
 *     컨텍스트 프레임워크(Spring TestContext Framework)를 통합하는 역할을 수행함.
 * </p>
 * <p>
 *     <code>@Import({Some.class})</code> -
 *     <code>@Autowired</code> 어노테이션을 통해 의존성 주입할 클래스를 import하여 
 *     가져오고 실제로 의존성 주입이 가능하게 하는 어노테이션
 * </p>
 * <p>
 *     아래 어노테이션들은 스프링 빈 객체를 주입받아 사용하는 방식의 테스트를 구성함.
 *     반면 기존의 UserServiceTest 클래스는 스프링 빈으로 등록하지 않고 직접 객체를
 *     생성하여 사용하는 방식. 
 * </p>
 */
@ExtendWith(SpringExtension.class)
@Import({UserService.class})
public class UserServiceTest2 {

    @MockitoBean
    private SiteUsersRepository siteUsersRepository;

    @MockitoBean
    private ClassTypeRepository classTypeRepository;

    @Autowired
    private UserService userService;

    /**
     * 각 테스트 케이스들 간의 독립성을 위해 매 테스트 케이스 때마다
     * mock 객체를 주입.
     */
    @BeforeEach
    public void setUp() {

        userService = new UserService(
            classTypeRepository, siteUsersRepository
        );

    }

    @Test
    @DisplayName("UserService#getOneUserById() 테스트")
    void getOneUserByIdTest() {

        // Given
        final int memberId = 100;

        ClassType classType = ClassType.builder()
            .classNumber(1)
            .className("VIP")
            .bonus(1000)
            .build();

        SiteUsers user = SiteUsers.builder()
            .memberId(memberId)
            .signUpDate(LocalDate.now())
            .mileage(1000)
            .username("kimquel")
            .averPurchase(123.5)
            .classType(classType)
            .build();

        // siteUsersRepository는 여기서는 mock(가짜) 객체기에
        // 아무 것도 설정하지 않으면 아무런 기능을 하지 않는다.
        // 따라서 여기서는 테스트를 위해 특정 입력값을 토대로 특정 메서드를 호출하면 (when)
        // 특정 결과(thenReturn)가 나오도록 설정함.
        // given().willReturn()과 동일.
        Mockito.when(siteUsersRepository.findById(memberId))
            .thenReturn(Optional.of(user));

        // When
        SiteUsers actualUser = userService.getOneUserById(memberId);

        // Then
        assertThat(actualUser.getMemberId()).isEqualTo(user.getMemberId());
        assertThat(actualUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(actualUser.getMileage()).isLessThan(2000);
        assertThat(actualUser.getClassType().getClassName())
            .isEqualTo("VIP");

        // siteUsersRepository#findById()가 실제로 호출되었는지 검증 시도함으로써
        // 이 테스트의 신뢰도를 높인다.
        verify(siteUsersRepository).findById(memberId);

        // 이 테스트 케이스 내에서 한 번도 호출된 적 없으므로
        // 다음의 코드는 테스트 실패로 이어짐.
        //verify(classTypeRepository).findById(any());
    }

}
