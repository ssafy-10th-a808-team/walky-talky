package com.ssafy.backend.record.dto.response;


import com.ssafy.backend.record.dto.mapping.PointsMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseViewDto {
    private String title;
    private String startTime;
    private String duration;
    private Double distance;
    private List<PointsMapping> points;
    private int starRating;
    private String comment;
    private String address;
}
