package konkuk.chacall.domain.foodtruck.domain.value;

import lombok.Getter;

import java.util.EnumSet;

@Getter
public enum AvailableQuantity {

    LESS_THAN_50("50인분 미만"),
    LESS_THAN_100("100인분 미만"),
    LESS_THAN_150("150인분 미만"),
    MORE_THAN_200("200인분 이상"),
    NEED_DISCUSSION("논의 필요");

    private final String value;

    AvailableQuantity(String value) {
        this.value = value;
    }

    // 요청된 수량 기준을 만족하는 트럭의 가능한 값 집합
    public static EnumSet<AvailableQuantity> acceptableFor(AvailableQuantity req) {
        if (req == null) return EnumSet.noneOf(AvailableQuantity.class);

        return switch (req) {
            case LESS_THAN_50  ->
                    EnumSet.of(LESS_THAN_50, LESS_THAN_100, LESS_THAN_150, MORE_THAN_200, NEED_DISCUSSION);
            case LESS_THAN_100 ->
                    EnumSet.of(LESS_THAN_100, LESS_THAN_150, MORE_THAN_200, NEED_DISCUSSION);
            case LESS_THAN_150 ->
                    EnumSet.of(LESS_THAN_150, MORE_THAN_200, NEED_DISCUSSION);
            case MORE_THAN_200 ->
                    EnumSet.of(MORE_THAN_200, NEED_DISCUSSION);
            case NEED_DISCUSSION ->
                    EnumSet.of(NEED_DISCUSSION);
        };
    }
}
