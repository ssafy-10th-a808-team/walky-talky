package com.ssafy.backend.record.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;

@Entity
@Table(name = "record")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name="member_seq")
    private Long memberSeq;

    @Column(name = "group_seq")
    private Long groupSeq;

    private String title;

    @Column(name = "star_rating", nullable = false)
    private int starRating;

    private String comment;

    @Column(name = "used_count")
    private Integer usedCount;

    @Column(name = "scraped_count")
    private Integer scrapedCount;

    private Double distance;

    private Duration duration;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "region_cd", length = 255)
    private String regionCd;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
