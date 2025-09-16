package konkuk.chacall.domain.member.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "푸드트럭 저장 상태 변경 요청")
public record UpdateFoodTruckSaveStatusRequest(
        @Schema(description = "푸드트럭 저장 요청 여부, (true: 저장 / false: 저장 취소)", example = "true")
        @NotNull(message = "푸드트럭 저장 요청 여부는 필수입니다.")
        Boolean isSavedRequest
) {
}
