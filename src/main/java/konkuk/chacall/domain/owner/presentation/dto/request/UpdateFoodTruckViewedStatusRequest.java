package konkuk.chacall.domain.owner.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckViewedStatus;

public record UpdateFoodTruckViewedStatusRequest(
        @Schema(description = "변경할 푸드트럭 표시 여부", example = "OFF")
        @NotNull(message = "푸드트럭 표시 상태는 필수입니다.")
        FoodTruckViewedStatus status
) {
}
