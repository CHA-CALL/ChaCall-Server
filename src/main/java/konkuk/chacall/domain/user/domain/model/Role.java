package konkuk.chacall.domain.user.domain.model;

import lombok.Getter;

@Getter
public enum Role {
    MEMBER("일반 회원"), OWNER("사장님");

    private final String value;

    Role(String value) {
        this.value = value;
    }
}
