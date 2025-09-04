package konkuk.chacall.domain.foodtruck.domain.value;

import lombok.Getter;

@Getter
public enum AvailableQuantity {

    LESS_THAN_50("50인분 미만"),
    LESS_THAN_100("100인분 미만"),
    LESS_THAN_150("150인분 미만"),
    LESS_THAN_200("200인분 미만"),
    MORE_THAN_200("200인분 이상"),
    UNDEFINED("미정"),
    NEED_DISCUSSION("논의 필요");

    private final String value;

    AvailableQuantity(String value) {
        this.value = value;
    }
}
