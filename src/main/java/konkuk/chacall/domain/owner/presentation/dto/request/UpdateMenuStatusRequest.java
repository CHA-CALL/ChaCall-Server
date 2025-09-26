package konkuk.chacall.domain.owner.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import konkuk.chacall.domain.foodtruck.domain.value.MenuViewedStatus;

public record UpdateMenuStatusRequest(
        @Schema(description = "변경할 메뉴 표시 여부", example = "OFF")
        @NotNull(message = "메뉴 상태는 필수입니다.")
        MenuViewedStatus status
) {}
