package konkuk.chacall.domain.foodtruck.domain.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import konkuk.chacall.global.common.dto.EnumValue;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
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

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PaymentMethod from(String value) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.getValue().equals(value)) {
                return method;
            }
        }
        throw new DomainRuleException(ErrorCode.PAYMENT_METHOD_MISMATCH);
    }
}
