package com.ssafy.backend.shareBoard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseScrapDto {
    private Long shareBoardSeq;
    private Long recordSeq;
    private boolean isScraped;
    private int scrapCount;
}
