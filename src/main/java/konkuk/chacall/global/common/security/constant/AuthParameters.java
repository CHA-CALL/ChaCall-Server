package konkuk.chacall.global.common.security.constant;

import lombok.Getter;

@Getter
public enum AuthParameters {
    JWT_HEADER_KEY("Authorization"),
    JWT_PREFIX("Bearer "),
    KAKAO("kakao"),
    KAKAO_PROVIDER_ID_KEY("id"),
    JWT_ACCESS_TOKEN_KEY("userId"),
    JWT_TOKEN_ATTRIBUTE("token"),
    REDIRECT_ROLE_SELECT_URL("/oauth2/role-select"),
    REDIRECT_MEMBER_HOME_URL("/member"),
    REDIRECT_OWNER_HOME_URL("/owner"),

    ;

    private final String value;

    AuthParameters(String value) {
        this.value = value;
    }
}

