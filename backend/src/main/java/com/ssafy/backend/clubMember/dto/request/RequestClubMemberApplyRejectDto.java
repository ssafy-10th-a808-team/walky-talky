package com.ssafy.backend.clubMember.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestClubMemberApplyRejectDto {
    private Long clubSeq;
    private Long memberSeq;

}
