package konkuk.chacall.domain.member.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "평점 등록 요청 DTO")
public record RegisterRatingRequest(
        @Schema(description = "예약 ID", example = "1")
        @NotNull(message = "예약 ID는 null일 수 없습니다.")
        Long reservationId,

        @Schema(description = "푸드트럭 ID", example = "1")
        @NotNull(message = "푸드트럭 ID는 null일 수 없습니다.")
        Long foodTruckId,

        @Schema(description = "평점 (0~5 범위 내에 0.5 단위)", example = "4.5")
        @NotNull(message = "평점은 null일 수 없습니다.")
        Double rating
) {
}
