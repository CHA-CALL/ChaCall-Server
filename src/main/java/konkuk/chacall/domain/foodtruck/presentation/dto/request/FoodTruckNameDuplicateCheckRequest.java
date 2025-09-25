package konkuk.chacall.domain.foodtruck.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record FoodTruckNameDuplicateCheckRequest(
        @Schema(description = "중복 여부를 확인할 푸드트럭 이름", example = "차콜 꼬치")
        @NotBlank(message = "푸드트럭 이름은 필수입니다.")
        String name
) {}
