package com.ssafy.backend.shareBoard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseShareBoardDto {
    private Long shareBoardSeq;
    private Long recordSeq;
    private String title;
    private String create_at;
    private int hit;
    private int commentCount;
}
