package com.ssafy.backend.shareBoard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMemberDto {
    private Long shareBoardSeq;
    private String nickname;
    private String profilePic;
}
