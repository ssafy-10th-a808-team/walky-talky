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
    private String address;
    private String duration;
    private Double distance;
    private List<PointsMapping> points;
    private String startTime;
    private int starRating;
    private String comment;
}
