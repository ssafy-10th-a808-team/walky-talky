package com.ssafy.backend.plan.dto.response;

import com.ssafy.backend.club.domain.Club;
import com.ssafy.backend.clubMember.domain.ClubMember;
import com.ssafy.backend.record.domain.Record;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePlanListDtoPlan {
    private Long seq;
    private Long clubSeq;
    private Long recordSeq;
    private Long clubMemberSeq;

    private String title;
    private String content;
    private Timestamp startTime;
    private Time duration;
    private String latitude;
    private String longitude;
    private Integer nowCapacity;
    private Integer maxCapacity;
}
