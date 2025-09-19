package konkuk.chacall.domain.reservation.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReservationIdResponse(
        @Schema(description = "생성된 예약 ID", example = "1")
        Long reservationId
) {
    public static ReservationIdResponse of(Long reservationId) {
        return new ReservationIdResponse(reservationId);
    }
}
