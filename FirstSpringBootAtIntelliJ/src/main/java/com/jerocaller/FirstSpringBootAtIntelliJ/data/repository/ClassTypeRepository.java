package com.jerocaller.FirstSpringBootAtIntelliJ.data.repository;

import com.jerocaller.FirstSpringBootAtIntelliJ.data.entity.ClassType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassTypeRepository extends JpaRepository<ClassType, Integer> {

}
