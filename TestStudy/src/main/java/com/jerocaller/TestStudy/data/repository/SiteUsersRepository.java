package com.jerocaller.TestStudy.data.repository;

import com.jerocaller.TestStudy.data.entity.ClassType;
import com.jerocaller.TestStudy.data.entity.SiteUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SiteUsersRepository extends JpaRepository<SiteUsers, Integer> {

    List<SiteUsers> findByClassType(ClassType classType);

    /**
     * 가장 많은 추천을 받은 유저 한명 조회
     * 
     * @return
     */
    @Query(value = """
        SELECT su
            FROM SiteUsers su
            WHERE su.recommBy IS NOT null
            GROUP BY su.recommBy
            HAVING COUNT(su) = (
                SELECT MAX(sub.recommendationCount)
                FROM (
                    SELECT COUNT(su2) AS recommendationCount
                    FROM SiteUsers su2
                    WHERE su2.recommBy IS NOT null
                    GROUP BY su2.recommBy
                ) sub
            )
    """)
    Optional<SiteUsers> findByRecommByMax();
}
