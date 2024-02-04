package com.ssafy.backend.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "streak")
@Setter
@NoArgsConstructor
@Builder
public class Streak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "member_seq")
    private Long memberSeq;

    @Column(name = "walk_date")
    private String walkDate;

    @Column(name = "walk_distance")
    private double walkDistance;

    public Streak(Long seq, Long memberSeq, String walkDate, double walkDistance) {
        this.seq = seq;
        this.memberSeq = memberSeq;
        this.walkDate = walkDate;
        this.walkDistance = walkDistance;
    }
}
