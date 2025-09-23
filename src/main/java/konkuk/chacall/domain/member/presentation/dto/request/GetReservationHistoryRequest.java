package konkuk.chacall.domain.member.presentation.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;
import konkuk.chacall.global.common.dto.HasPaging;
import konkuk.chacall.global.common.dto.CursorPagingRequest;

public record GetReservationHistoryRequest(
        @Parameter(
                name = "status", in = ParameterIn.QUERY,
                description = "조회할 예약 상태",
                required = true,
                schema = @Schema(
                        type = "string",
                        example = "예약 대기",
                        allowableValues = {
                                "예약 대기",
                                "예약 확정 완료",
                                "예약 확정 요청",
                                "예약 취소 완료",
                                "예약 취소 요청"
                        }
                )
        )
        @NotNull(message = "예약 상태는 필수입니다.")
        ReservationStatus status
        ,

        @Valid
        CursorPagingRequest cursorPagingRequest
) implements HasPaging {
}
