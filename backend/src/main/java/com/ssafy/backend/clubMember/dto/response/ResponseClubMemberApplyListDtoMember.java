package com.ssafy.backend.clubMember.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseClubMemberApplyListDtoMember {
    private Long memberSeq;
    private String url;
    private String nickname;
    private String birth;
    private String introduce;
    private String address;
}
