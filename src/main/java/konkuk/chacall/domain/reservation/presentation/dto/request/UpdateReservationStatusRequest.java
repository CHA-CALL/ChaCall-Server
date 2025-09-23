package konkuk.chacall.domain.reservation.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;

public record UpdateReservationStatusRequest (
        @Schema(description = "변경할 예약 상태",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "예약 대기",
                allowableValues = {"예약 대기", "예약 확정 완료", "예약 확정 요청", "예약 취소 완료", "예약 취소 요청"})
        @NotNull(message = "예약 상태는 필수 입력 값입니다.")
        ReservationStatus reservationStatus
) {
}
