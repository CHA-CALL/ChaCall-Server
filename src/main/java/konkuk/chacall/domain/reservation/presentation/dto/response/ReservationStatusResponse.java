package konkuk.chacall.domain.reservation.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;

public record ReservationStatusResponse(
        @Schema(description = "예약 상태", example = "CONFIRMED")
        ReservationStatus reservationStatus
) {
}
