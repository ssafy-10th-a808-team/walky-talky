package com.ssafy.backend.shareBoard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCommentDto {
    private Long shareBoardSeq;
    private ResponseMemberDto member;
    private String content;
    private String created_at;
}
