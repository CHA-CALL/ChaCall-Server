package konkuk.chacall.domain.foodtruck.domain.value;

import lombok.Getter;

@Getter
public enum NeedElectricity {
    REQUIRED("O"),
    NOT_REQUIRED("X"),
    NEED_DISCUSSION("논의 필요");

    private final String value;

    NeedElectricity(String value) {
        this.value = value;
    }
}
