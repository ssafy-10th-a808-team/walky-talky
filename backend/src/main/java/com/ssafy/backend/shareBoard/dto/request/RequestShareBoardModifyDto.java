package com.ssafy.backend.shareBoard.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestShareBoardModifyDto {
    private String title;
    private String content;
}
