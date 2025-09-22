package konkuk.chacall.domain.reservation.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;

public record UpdateReservationStatusRequest(
        @Schema(description = "변경할 예약 상태",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "CONFIRMED",
                allowableValues = {"CONFIRMED", "CONFIRMED_REQUESTED", "CANCELLED", "CANCELLED_REQUESTED"})
        @NotNull(message = "예약 상태는 필수 입력 값입니다.")
        ReservationStatus reservationStatus
) {
}
