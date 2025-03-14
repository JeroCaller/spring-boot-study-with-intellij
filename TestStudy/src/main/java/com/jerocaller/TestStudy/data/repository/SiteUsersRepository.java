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
    @Query("""
        SELECT su1
        FROM SiteUsers su1
        JOIN SiteUsers su2
        ON su2.recommBy = su1.username
        WHERE
            su2 = (SELECT su3
                FROM SiteUsers su3
                WHERE su3.recommBy IS NOT NULL
                GROUP BY su3.recommBy
                HAVING COUNT(su3) = (
                    SELECT MAX(sub.recommendationCount)
                    FROM (
                        SELECT COUNT(su4) AS recommendationCount
                        FROM SiteUsers su4
                        WHERE su4.recommBy IS NOT NULL
                        GROUP BY su4.recommBy
                    ) sub
                )
            )
    """)
    Optional<SiteUsers> findByRecommByMax();
}
