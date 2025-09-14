package konkuk.chacall.global.common.security.oauth2.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthTokenRequest(
        @NotBlank(message = "loginTokenKey는 필수 파라미터입니다.")
        String loginTokenKey
) {
}
