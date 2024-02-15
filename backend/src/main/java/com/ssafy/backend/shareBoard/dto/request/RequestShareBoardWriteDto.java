package com.ssafy.backend.shareBoard.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestShareBoardWriteDto {
    private Long recordSeq;
    private String title;
    private String content;
}
