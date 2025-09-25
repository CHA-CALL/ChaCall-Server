package konkuk.chacall.domain.foodtruck.domain.value;

import lombok.Getter;

@Getter
public enum MenuStatus {
    ON("표시"),
    OFF("미표시");

    private final String value;

    MenuStatus(String value) {
        this.value = value;
    }
}
