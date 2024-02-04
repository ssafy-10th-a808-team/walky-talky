package com.ssafy.backend.plan.dto.request;

import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestPlanModifyDto {
    private Long planSeq;
    private Long recordSeq;
    private String title;
    private String content;
    private Timestamp startTime;
    private Time duration;
    private String latitude;
    private String longitude;
    private Integer maxCapacity;
}
