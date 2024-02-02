package com.ssafy.backend.plan.domain;

import com.ssafy.backend.club.domain.Club;
import com.ssafy.backend.clubMember.domain.ClubMember;
import com.ssafy.backend.global.domain.BaseEntity;
import com.ssafy.backend.record.domain.Record;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "plan")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plan extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "club_seq")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "record_seq")
    private Record record;

    @ManyToOne
    @JoinColumn(name = "club_member_seq")
    private ClubMember clubMember;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column
    private Time duration;

    private String latitude;
    private String longitude;

    @Column(name = "now_capacity")
    private Integer nowCapacity;

    @Column(name = "max_capacity")
    private Integer maxCapacity;
}
