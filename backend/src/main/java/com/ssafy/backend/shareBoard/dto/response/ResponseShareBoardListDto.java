package com.ssafy.backend.shareBoard.dto.response;

import com.ssafy.backend.record.dto.PointMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseShareBoardListDto {
    private String nickname;
    private String url;
    private String title;
    private String address;
    private List<PointMapping> points;
    private String duration;
    private Double distance;
    private Date create_at;
    private int likeCount;
    private int commentCount;
    private int scrapCount;
    private int hit;
}
