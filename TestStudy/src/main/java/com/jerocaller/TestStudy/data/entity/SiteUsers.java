package com.jerocaller.TestStudy.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "site_users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SiteUsers {

    @Id
    @Column(length = 11, nullable = false)
    private int memberId;

    private LocalDate signUpDate;

    @Column(length = 11)
    private int mileage;

    @Column(length = 50)
    private String username;

    private Double averPurchase;

    @Column(length = 50)
    private String recommBy;

    @ManyToOne
    @JoinColumn(name = "class_number")
    private ClassType classType;

}
