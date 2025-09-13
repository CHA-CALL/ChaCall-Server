package konkuk.chacall.global.common.security.oauth2;

import java.util.LinkedHashMap;
import java.util.Map;

import static konkuk.chacall.global.common.security.constant.AuthParameters.KAKAO;
import static konkuk.chacall.global.common.security.constant.AuthParameters.KAKAO_PROVIDER_ID_KEY;

public class KakaoUserDetails implements OAuth2UserDetails {

    private final Map<String, Object> attributes;

    public KakaoUserDetails(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return KAKAO.getValue();
    }

    @Override
    public String getProviderId() {
        return attributes.get(KAKAO_PROVIDER_ID_KEY.getValue()).toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getNickname() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        return profile.get("nickname").toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getProfileImage() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        return profile.get("profile_image_url").toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getEmail() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        return account.get("email").toString();
    }
}
