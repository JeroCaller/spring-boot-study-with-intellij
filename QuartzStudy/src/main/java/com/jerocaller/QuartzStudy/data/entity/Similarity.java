package com.jerocaller.QuartzStudy.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "similarity")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Similarity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private double similarityValue;

    @ManyToOne
    @JoinColumn(name = "news_source_id")
    private News sourceNews;

    @ManyToOne
    @JoinColumn(name = "news_target_id")
    private News targetNews;
}
