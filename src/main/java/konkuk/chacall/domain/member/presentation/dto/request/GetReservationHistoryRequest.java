package konkuk.chacall.domain.member.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;
import konkuk.chacall.global.common.dto.HasPaging;
import konkuk.chacall.global.common.dto.CursorPagingRequest;

public record GetReservationHistoryRequest(
        @Schema(description = "조회할 예약 상태",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "PENDING",
                allowableValues = {"PENDING", "CONFIRMED", "CANCELLED"})
        @NotNull(message = "예약 상태는 필수입니다.")
        ReservationStatus status,

        @Valid
        CursorPagingRequest cursorPagingRequest
) implements HasPaging {
}
