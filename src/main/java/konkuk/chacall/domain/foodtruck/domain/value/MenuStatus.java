package konkuk.chacall.domain.foodtruck.domain.value;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum MenuStatus {
    ON("노출"),
    OFF("비노출");

    @JsonValue
    private final String value;

    MenuStatus(String value) {
        this.value = value;
    }
}
