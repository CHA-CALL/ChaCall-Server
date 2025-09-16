package konkuk.chacall.domain.member.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "평점 등록 요청 DTO")
public record RegisterRatingRequest(
        @Schema(description = "예약 ID", example = "1")
        @NotNull(message = "예약 ID는 null일 수 없습니다.")
        Long reservationId,

        @Schema(description = "푸드트럭 ID", example = "1")
        @NotNull(message = "푸드트럭 ID는 null일 수 없습니다.")
        Long foodTruckId,

        @Schema(description = "평점 (0~5 범위 내에 0.5 단위)", example = "4.5")
        @Pattern(
                regexp = "^(?:[0-4](?:\\.0|\\.5)|5(?:\\.0)?)$",
                message = "평점은 0.0부터 5.0까지 0.5 단위로 입력해야 합니다."
        )

        @NotNull(message = "평점은 null일 수 없습니다.")
        String rating
) {
}
