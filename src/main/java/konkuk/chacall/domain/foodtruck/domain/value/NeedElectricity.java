package konkuk.chacall.domain.foodtruck.domain.value;

import konkuk.chacall.global.common.dto.EnumValue;
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
}
