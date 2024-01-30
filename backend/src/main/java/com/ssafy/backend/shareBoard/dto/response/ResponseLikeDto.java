package com.ssafy.backend.shareBoard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseLikeDto {
    private Long shareBoardSeq;
    private boolean isLiked;
    private int likeCount;
}
