package com.jerocaller.TestStudy.business;

import com.jerocaller.TestStudy.common.ResponseMessages;
import com.jerocaller.TestStudy.data.entity.ClassType;
import com.jerocaller.TestStudy.data.entity.SiteUsers;
import com.jerocaller.TestStudy.data.repository.ClassTypeRepository;
import com.jerocaller.TestStudy.data.repository.SiteUsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final ClassTypeRepository classTypeRepository;
    private final SiteUsersRepository siteUsersRepository;

    /**
     * 현재 DB에 있는 모든 클래스 타입 정보들을 조회한다.
     *
     * @return
     */
    public List<ClassType> getAllClassTypes() {
        return classTypeRepository.findAll();
    }

    /**
     * 모든 유저들의 정보 반환
     * 
     * @return
     */
    public List<SiteUsers> getAllUsers() {
        return siteUsersRepository.findAll();
    }

    /**
     * id를 이용하여 특정 유저 정보 조회
     *
     * @param id - PK
     * @return
     */
    public SiteUsers getOneUserById(int id) {
        return siteUsersRepository.findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(ResponseMessages.NO_USER_FOUND)
            );
    }

    /**
     * 특정 클래스 타입에 해당하는 모든 유저 조회
     * 
     * @param classTypeId - 조회하고자 하는 클래스 타입
     * @return
     */
    public List<SiteUsers> getUsersByClassType(int classTypeId) {
        
        ClassType targetClassType = classTypeRepository.findById(classTypeId)
            .orElseThrow(() ->
                new EntityNotFoundException("조회된 클래스 타입 없음")
            );

        return siteUsersRepository.findByClassType(targetClassType);
    }

    /**
     * 가장 많은 추천을 받은 유저 한 명 조회.
     *
     * @return
     */
    public SiteUsers getUserByMaxRecomm() {
        return siteUsersRepository.findByRecommByMax()
            .orElseThrow(() ->
                new EntityNotFoundException(ResponseMessages.NO_USER_FOUND)
            );
    }

}
