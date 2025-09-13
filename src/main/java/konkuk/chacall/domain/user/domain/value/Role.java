package konkuk.chacall.domain.user.domain.value;

import lombok.Getter;

@Getter
public enum Role {
    MEMBER("일반 회원"),
    OWNER("사장님"),
    NON_SELECTED("선택 안함")
    ;

    private final String value;

    Role(String value) {
        this.value = value;
    }
}
