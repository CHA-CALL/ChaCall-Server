package konkuk.chacall.domain.user.domain.model;

import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Gender {
    M("남성"), F("여성");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public static Gender from(String value) {
        return Arrays.stream(Gender.values())
                .filter(genderVal -> genderVal.getValue().equals(value.trim()))
                .findFirst()
                .orElseThrow(
                        () -> new DomainRuleException(ErrorCode.USER_GENDER_MISMATCH,
                                new IllegalArgumentException(
                                        String.format("존재하지 않는 성별입니다. value: %s", value)
                                ))
                );
    }
}
