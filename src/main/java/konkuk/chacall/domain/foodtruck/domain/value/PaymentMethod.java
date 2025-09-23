package konkuk.chacall.domain.foodtruck.domain.value;

import konkuk.chacall.global.common.dto.EnumValue;
import lombok.Getter;

@Getter
public enum PaymentMethod implements EnumValue {
    ANY("무관"),
    BANK_TRANSFER("계좌이체"),
    CARD("카드");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }
}
