package com.jerocaller.TestStudy.data.repository;

import com.jerocaller.TestStudy.data.entity.ClassType;
import com.jerocaller.TestStudy.data.entity.SiteUsers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>DB와 가장 가까운 repository에 대한 테스트</p>
 * <p>어노테이션 설명</p>
 * <p>
 *     <code>@DataJpaTest</code> - 다음의 기능들을 수행한다.
 *     <ol>
 *     <li>JPA 관련된 설정들만 로드하여 테스트 진행함.</li>
 *     <li>기본적으로 <code>@Transactional</code> 어노테이션을 포함하고 있어
 *     테스트 코드 종료 시 자동으로 DB의 Rollback이 진행됨.
 *     => 이로 인해 테스트 진행 과정에서 테스트용 데이터를 생성하여 save() 메서드 등을
 *     호출하면 테스트 종료 후 롤백되기에 결과적으로는 DB에 테스트 데이터가 남지 않는다.
 *     따라서 별도로 테스트 데이터를 지우는 기능을 정의하지 않아도 된다.
 *     </li>
 *     <li>기본값으로는 H2와 같은 Embedded DB를 사용한다. 다른 DB 사용 시 아래 소개할
 *     어노테이션을 통해 별도 설정을 해야 한다.
 *     </li>
 *     </ol>
 * </p>
 *
 * <p>
 *     <code>@AutoConfigureTestDatabase(replace = Replace.NONE)</code> -
 *     어떤 종류의 데이터베이스를 사용할 것인지 설정하기 위한 어노테이션
 *     <ul>
 *         <li>
 *             ANY : H2와 같은 Embedded memory DB 사용 시
 *         </li>
 *         <li>
 *             NONE : 애플리케이션에서 실제 사용하는 DB로 테스트. 여기선
 *             MariaDB를 사용하므로 이를 테스트에 사용하게끔 함.
 *         </li>
 *     </ul>
 * </p>
 *
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class SiteUsersRepositoryTest {

    @Autowired
    private SiteUsersRepository siteUsersRepository;

    @Autowired
    private ClassTypeRepository classTypeRepository;

    @Test
    @DisplayName("findByClassType: 특정 클래스 타입의 유저만 조회하는지 테스트")
    public void findByClassTypeTest() {

        ClassType givenTypeOne = classTypeRepository.saveAndFlush(
            ClassType.builder()
                .classNumber(50)
                .className("VIP")
                .build()
        );
        ClassType givenTypeTwo = classTypeRepository.saveAndFlush(
            ClassType.builder()
                .classNumber(60)
                .className("GOLD")
                .build()
        );

        log.info("givenTypeOne: {}", givenTypeOne);
        log.info("givenTypeTwo: {}", givenTypeTwo);

        SiteUsers givenTypeOneUser = siteUsersRepository.saveAndFlush(
            SiteUsers.builder()
                .memberId(100)
                .username("kimquel")
                .classType(givenTypeOne)
                .build()
        );
        SiteUsers givenTypeTwoUser = siteUsersRepository.saveAndFlush(
            SiteUsers.builder()
                .memberId(200)
                .username("jeongdb")
                .classType(givenTypeTwo)
                .build()
        );

        log.info("givenTypeOneUser: {}", givenTypeOneUser);
        log.info("givenTypeTwoUser: {}", givenTypeTwoUser);

        List<SiteUsers> resultUsers =
            siteUsersRepository.findByClassType(givenTypeOne);
        log.info("size : {}", resultUsers.size());
        logList(resultUsers);

        assertThat(resultUsers.getFirst().getClassType().getClassNumber())
            .isEqualTo(50);
        assertThat(resultUsers.getFirst().getClassType().getClassName())
            .isEqualTo("VIP");
        assertThat(resultUsers.getFirst().getMemberId()).isEqualTo(100);
        assertThat(resultUsers.getFirst().getUsername())
            .isEqualTo("kimquel");
        assertThat("jeongDb").isNotIn(resultUsers);

    }

    @Test
    @DisplayName("findByRecommByMax: 최다 추천받은 사용자 조회 기능 테스트")
    public void findBYRecommByMaxTest() {

        SiteUsers givenUserOne = siteUsersRepository.saveAndFlush(
            SiteUsers.builder()
                .memberId(100)
                .username("kimquel")
                .recommBy("jeongdb")
                .build()
        );
        SiteUsers givenUserTwo = siteUsersRepository.saveAndFlush(
            SiteUsers.builder()
                .memberId(101)
                .username("jeongdb")
                .recommBy("kimquel")
                .build()
        );
        SiteUsers givenUserThree = siteUsersRepository.saveAndFlush(
            SiteUsers.builder()
                .memberId(102)
                .username("javas")
                .recommBy("kimquel")
                .build()
        );

        SiteUsers resultUser = siteUsersRepository.findByRecommByMax()
            .orElseGet(null);

        assertThat(resultUser).isNotNull();
        assertThat(resultUser.getUsername()).isEqualTo("kimquel");

    }

    private <E> void logList(List<E> list) {

        log.info("=== List Logging Start ===");

        if (list.isEmpty()) {
            log.info("Nothing Here!!!");
        } else {
            for (int i = 0; i < list.size(); i++) {
                log.info("{} : {}", i, list.get(i));
            }
        }

        log.info("=== List Logging End ===");

    }

}