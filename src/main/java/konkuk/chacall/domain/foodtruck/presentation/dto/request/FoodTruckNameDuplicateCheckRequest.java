package konkuk.chacall.domain.foodtruck.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FoodTruckNameDuplicateCheckRequest(
        @Schema(description = "중복 여부를 확인할 푸드트럭 이름", example = "차콜 꼬치")
        @NotBlank(message = "푸드트럭 이름은 필수입니다.")
        @Size(max = 10, message = "푸드트럭 이름은 최대 10글자입니다.")
        String name
) {
}
