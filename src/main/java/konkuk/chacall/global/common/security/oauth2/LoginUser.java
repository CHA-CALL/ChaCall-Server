package konkuk.chacall.global.common.security.oauth2;

import konkuk.chacall.domain.user.domain.value.Role;

public record LoginUser(
    String kakaoId,
    Long userId,
    Role role
) {
    public static LoginUser createLoginUser(String kakaoId, Long userId, Role role) {
        return new LoginUser(kakaoId, userId, role);
    }

    public static LoginUser createLoginUser(Long userId) {
        return new LoginUser(null, userId, null);
    }
}
