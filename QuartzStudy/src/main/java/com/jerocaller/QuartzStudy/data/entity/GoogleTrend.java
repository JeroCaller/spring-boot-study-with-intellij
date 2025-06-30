package com.jerocaller.QuartzStudy.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "google_trend")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GoogleTrend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 30)
    private String title;

    @Setter
    private LocalDateTime pubDate;

    @Column(length = 200)
    private String imageUrl;

    @Column(length = 100)
    @Setter
    private String imageSource;

    @Setter
    private Integer leastTraffic;

    @Builder.Default
    private Integer calledCounter = 1;

    public void updateCounter() {
        ++calledCounter;
    }
}
