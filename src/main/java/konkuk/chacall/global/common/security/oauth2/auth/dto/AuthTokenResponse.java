package konkuk.chacall.global.common.security.oauth2.auth.dto;

public record AuthTokenResponse(
        String token
) {
    public static AuthTokenResponse of(String token) {
        return new AuthTokenResponse(token);
    }
}
