package com.ssafy.backend.plan.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Timestamp startTime;
    private Time duration;
    private String latitude;
    private String longitude;
    private Integer maxCapacity;
    private String location;
}
