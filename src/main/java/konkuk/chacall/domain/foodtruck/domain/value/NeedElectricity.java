package konkuk.chacall.domain.foodtruck.domain.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import konkuk.chacall.global.common.dto.EnumValue;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public enum NeedElectricity implements EnumValue {
    REQUIRED("필요"),
    NOT_REQUIRED("불필요"),
    NEED_DISCUSSION("논의 필요");

    private final String value;

    NeedElectricity(String value) {
        this.value = value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static NeedElectricity from(String value) {
        for (NeedElectricity needElectricity : NeedElectricity.values()) {
            if (needElectricity.getValue().equals(value)) {
                return needElectricity;
            }
        }
        throw new DomainRuleException(ErrorCode.NEED_ELECTRICITY_MISMATCH);
    }
}
