package com.ssafy.backend.clubMember.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseClubMemberApplyListDtoMember {
    private Long memberSeq;
    private String url;
    private String nickname;
    private String birth;
    private String introduce;
    private String address;
}
