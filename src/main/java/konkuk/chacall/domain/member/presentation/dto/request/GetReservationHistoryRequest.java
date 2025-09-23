package konkuk.chacall.domain.member.presentation.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;
import konkuk.chacall.domain.reservation.domain.value.ReservationViewType;
import konkuk.chacall.global.common.dto.HasPaging;
import konkuk.chacall.global.common.dto.CursorPagingRequest;

public record GetReservationHistoryRequest(
        @Parameter(
                name = "viewType", in = ParameterIn.QUERY,
                description = "조회할 예약 내역 타입",
                required = true,
                schema = @Schema(
                        type = "string",
                        example = "예약 대기",
                        allowableValues = {
                                "진행 예정",
                                "확정 신청",
                                "완료 내역",
                                "취소 내역"
                        }
                )
        )
        @NotNull(message = "예약 내역 타입은 필수입니다.")
        ReservationViewType viewType
        ,

        @Valid
        CursorPagingRequest cursorPagingRequest
) implements HasPaging {
}
