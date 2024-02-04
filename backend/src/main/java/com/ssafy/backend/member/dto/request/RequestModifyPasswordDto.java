package com.ssafy.backend.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestModifyPasswordDto {
    private String password;
    private String newPassword;
    private String checkNewPassword;
}
