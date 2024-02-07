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
public class ResponseListDto {
    private Long recordSeq;
    private String title;
    private List<PointsMapping> points;
    private int starRating;
    private String comment;
    private Double distance;
    private String duration;
}
