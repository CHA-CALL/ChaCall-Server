package konkuk.chacall.global.common.security.oauth2;

// 카카오 소셜 로그인 통합을 위한 인터페이스
public interface OAuth2UserDetails {
    String getProvider(); // (e.g., "kakao", "google", etc.)
    String getProviderId();
    String getNickname();
    String getProfileImage();
    String getEmail();
}
