package com.jerocaller.TestStudy.business;

import com.jerocaller.TestStudy.data.entity.ClassType;
import com.jerocaller.TestStudy.data.entity.SiteUsers;
import com.jerocaller.TestStudy.data.repository.ClassTypeRepository;
import com.jerocaller.TestStudy.data.repository.SiteUsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Service 계층에 존재하는 Bean에 대한 테스트.
 *
 * 여기서는 컨트롤러와 달리 웹 통신을 통한 테스트가 필요하지 않고 오로지
 * 각 메서드에 대한 기능적 테스트만 필요하다고 가정. 이 경우 단위 테스트로 진행됨.
 * 이 경우 독립적인 테스트를 통한 정확한 테스트 결과 도출을 위해,
 * Service 객체에는 외부 의존성이 들어가지 않게 하고 대신 가짜 의존성을 넣도록 하여
 * 외부 의존성과 독립적으로 테스트하도록 설계.
 */
@Slf4j
class UserServiceTest {

    private final SiteUsersRepository siteUsersRepository =
        Mockito.mock(SiteUsersRepository.class);
    private final ClassTypeRepository classTypeRepository =
        Mockito.mock(ClassTypeRepository.class);
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

    @Test
    @DisplayName("""
        UserService#getUsersByClassType - 여러 클래스 타입을 가지는 
        유저들 중 특정 클래스 타입을 가진 유저만 조회 가능한지 여부 테스트.
    """)
    void getUsersByClassTypeTest() {

        ClassType givenTypeOne = ClassType.builder()
            .classNumber(1)
            .className("VVIP")
            .bonus(3000)
            .build();

        ClassType givenTypeTwo = ClassType.builder()
            .classNumber(2)
            .className("VIP")
            .bonus(2000)
            .build();

        ClassType givenTypeThree = ClassType.builder()
            .classNumber(3)
            .className("Gold")
            .bonus(1000)
            .build();

        Map<Integer, SiteUsers> users = new HashMap<>();
        users.put(1, SiteUsers.builder()
            .memberId(1)
            .username("kimquel")
            .classType(givenTypeThree)
            .build()
        );
        users.put(2, SiteUsers.builder()
            .memberId(2)
            .username("jeongdb")
            .classType(givenTypeTwo)
            .build()
        );
        users.put(3, SiteUsers.builder()
            .memberId(3)
            .username("javas")
            .classType(givenTypeOne)
            .build()
        );
        users.put(4, SiteUsers.builder()
            .memberId(4)
            .username("naithon")
            .classType(givenTypeOne)
            .build()
        );

        final int targetClassNumber = 1;

        when(classTypeRepository.findById(1))
            .thenReturn(Optional.of(givenTypeOne));
        when(classTypeRepository.findById(2))
            .thenReturn(Optional.of(givenTypeTwo));
        when(classTypeRepository.findById(3))
            .thenReturn(Optional.of(givenTypeThree));

        when(siteUsersRepository.findByClassType(givenTypeOne))
            .thenReturn(Arrays.asList(users.get(3), users.get(4)));
        when(siteUsersRepository.findByClassType(givenTypeTwo))
            .thenReturn(Collections.singletonList(users.get(2)));
        when(siteUsersRepository.findByClassType(givenTypeThree))
            .thenReturn(Collections.singletonList(users.get(1)));

        List<SiteUsers> typeOneUsers = userService.getUsersByClassType(1);
        List<SiteUsers> typeTwoUsers = userService.getUsersByClassType(2);
        List<SiteUsers> typeThreeUsers = userService.getUsersByClassType(3);

        assertThat(typeOneUsers.size()).isEqualTo(2);
        assertThat(users.get(3)).isIn(typeOneUsers);
        assertThat(users.get(4)).isIn(typeOneUsers);
        assertThat(typeOneUsers.getFirst().getClassType().getClassName())
            .isEqualTo("VVIP");

        assertThat(typeTwoUsers.size()).isEqualTo(1);
        assertThat(typeTwoUsers.getFirst().getUsername()).isEqualTo("jeongdb");
        assertThat(typeTwoUsers.getFirst().getClassType().getClassName())
            .isEqualTo("VIP");

        assertThat(typeThreeUsers.size()).isEqualTo(1);
        assertThat(typeThreeUsers.getFirst().getUsername()).isEqualTo(
            "kimquel");
        assertThat(typeThreeUsers.getFirst().getClassType().getClassName())
            .isEqualTo("Gold");

        log.info("=== TypeUsers ===");
        log.info("TypeOneUsers) ");
        logList(typeOneUsers);
        log.info("TypeTwoUsers)");
        logList(typeTwoUsers);
        log.info("TypeThreeUsers");
        logList(typeThreeUsers);
        log.info("=== TypeUsers End ===");

        log.info("=== user map ===");
        logMap(users);

        verify(classTypeRepository, times(3)).findById(anyInt());
        verify(siteUsersRepository, times(3))
            .findByClassType(any(ClassType.class));

    }

    private <T> void logList(List<T> list) {

        list.forEach(element -> {
            log.info(element.toString());
        });

    }

    private <K, V> void logMap(Map<K, V> map) {

        map.forEach((key, value) -> {
            log.info("{} : {}", key.toString(), value.toString());
        });

    }

}