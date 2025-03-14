package com.jerocaller.TestStudy.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "classes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ClassType {

    @Id
    @Column(nullable = false, length = 11)
    private int classNumber;

    @Column(length = 20)
    private String className;

    @Column(length = 11)
    private Integer bonus;

}
