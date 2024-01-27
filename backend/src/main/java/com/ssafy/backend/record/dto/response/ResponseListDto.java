package com.ssafy.backend.record.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseListDto {
    private Long seq;
    private String title;
    private int starRating;
    private String comment;
    private Double distance;
    private String duration;
}
