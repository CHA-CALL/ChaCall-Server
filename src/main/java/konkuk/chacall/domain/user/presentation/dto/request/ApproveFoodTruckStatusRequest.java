package konkuk.chacall.domain.user.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckStatus;

public record ApproveFoodTruckStatusRequest(
        @Schema(description = "변경할 푸드트럭 승인 상태", example = "OFF")
        @NotNull(message = "승인 상태는 필수입니다.")
        FoodTruckStatus status
) {}
