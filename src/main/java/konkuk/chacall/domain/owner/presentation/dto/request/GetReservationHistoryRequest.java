package konkuk.chacall.domain.owner.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;

public record GetReservationHistoryRequest(
        @Schema(description = "조회할 예약 상태",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "PENDING")
        @NotBlank(message = "예약 상태는 필수입니다.")
        @Pattern(regexp = "^(PENDING|CONFIRMED|CANCELLED)$", message = "유효하지 않은 예약 상태입니다.")
        ReservationStatus status,

        @Schema(description = "마지막으로 조회된 예약의 ID (다음 페이지 요청 시 사용)",
                example = "120",
                nullable = true)
        Long cursor,

        @Schema(description = "한 페이지에 조회할 개수",
                example = "30",
                defaultValue = "20",
                minimum = "1",
                nullable = true)
        @Min(value = 1, message = "size 는 1 이상이어야 합니다.")
        Integer size
) {
    /**
     * size 가 null 이면 기본값 20을, 아니면 그 값을 반환
     *
     * @return int 타입의 페이지 크기
     */
    public int getPageSizeOrDefault() {
        return (this.size == null) ? 20 : this.size;
    }

    /**
     * cursor 가 null 이면 기본값 : Long 의 최대값을, 아니면 그 값을 반환
     *
     * @return Long 타입의 커서값
     */
    public Long getCursorOrDefault() {
        return (this.cursor == null) ? Long.MAX_VALUE : this.cursor;
    }
}
