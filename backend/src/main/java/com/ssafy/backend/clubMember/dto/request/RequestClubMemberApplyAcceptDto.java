package com.ssafy.backend.clubMember.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RequestClubMemberApplyAcceptDto {
    private Long clubSeq;
    private Long memberSeq;
}
