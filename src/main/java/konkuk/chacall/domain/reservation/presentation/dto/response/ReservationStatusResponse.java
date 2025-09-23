package konkuk.chacall.domain.reservation.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReservationStatusResponse(
        @Schema(description = "예약 상태", example = "예약 확정")
        String reservationStatus
) {
}
