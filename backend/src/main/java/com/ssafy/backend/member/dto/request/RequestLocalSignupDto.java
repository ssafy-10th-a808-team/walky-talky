package com.ssafy.backend.member.dto.request;

import com.ssafy.backend.member.domain.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestLocalSignupDto {

    private MultipartFile multipartFile;
    private String id;
    private String password;
    private String birth;
    private String gender;
    private String nickname;
    private String introduce;
    private String regionCd;
    private String imgUrl;
    private String loginType;

    public Member toEntity() {
        return Member.builder()
                .seq(null)
                .loginType("local")
                .memberId(id)
                .password(password)
                .birth(birth)
                .gender(gender)
                .nickname(nickname)
                .url(imgUrl)
                .introduce(introduce)
                .regionCd(regionCd)
                .isAlert(true)
                .isDeleted(false)
                .build();
    }

    // Null 체크 메서드
    public boolean isNull(Object value) {
        return value == null;
    }

    // Empty 체크 메서드
    public boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }

        if (value instanceof String) {
            return ((String) value).trim().isEmpty();
        }

        if (value instanceof Collection) {
            return ((Collection<?>) value).isEmpty();
        }

        if (value instanceof Map) {
            return ((Map<?, ?>) value).isEmpty();
        }

        if (value.getClass().isArray()) {
            return ((Object[]) value).length == 0;
        }

        return false;
    }

    // DTO 전체의 Null 체크
    public boolean isAnyFieldNull() {
        return isNull(id) || (isNull(loginType) && isNull(password)) || isNull(birth) || isNull(gender) ||
                isNull(nickname) || isNull(regionCd);
    }

    // DTO 전체의 Empty 체크
    public boolean isAnyFieldEmpty() {
        return isEmpty(id) || (isEmpty(loginType) && isEmpty(password)) || isEmpty(birth) || isEmpty(gender) ||
                isEmpty(nickname) || isEmpty(regionCd);
    }
}
