package com.ssafy.backend.plan.dto.response;

import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePlanDetailDtoPlan {
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
    private String location;
}
