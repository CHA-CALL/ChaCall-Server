package konkuk.chacall.domain.member.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "푸드트럭 저장 상태 변경 요청")
public record UpdateFoodTruckSaveStatusRequest(
    @Schema(description = "푸드트럭 저장 요청 여부, (true: 저장 / false: 저장 취소)", example = "true")
        boolean isSavedRequest
) {
}
