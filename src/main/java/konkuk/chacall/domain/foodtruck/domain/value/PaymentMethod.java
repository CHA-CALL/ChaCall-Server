package konkuk.chacall.domain.foodtruck.domain.value;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    ANY("무관"),
    BANK_TRANSFER("입금"),
    CARD("카드");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }
}
