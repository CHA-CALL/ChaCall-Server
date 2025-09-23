package konkuk.chacall.domain.reservation.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.chacall.domain.reservation.application.ReservationService;
import konkuk.chacall.domain.reservation.presentation.dto.request.CreateReservationRequest;
import konkuk.chacall.domain.reservation.presentation.dto.request.UpdateReservationRequest;
import konkuk.chacall.domain.reservation.presentation.dto.request.UpdateReservationStatusRequest;
import konkuk.chacall.domain.reservation.presentation.dto.response.ReservationIdResponse;
import konkuk.chacall.domain.reservation.presentation.dto.response.ReservationResponse;
import konkuk.chacall.domain.reservation.presentation.dto.response.ReservationStatusResponse;
import konkuk.chacall.global.common.annotation.ExceptionDescription;
import konkuk.chacall.global.common.annotation.UserId;
import konkuk.chacall.global.common.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static konkuk.chacall.global.common.swagger.SwaggerResponseDescription.*;

@Tag(name = "Reservation API", description = "예약 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(
            summary = "예약 견적서 작성 (예약 생성)",
            description = "사장님이 예약 견적서를 작성합니다. 예약 견적서 정보에 따라 예약이 생성됩니다. (예약 상태: 예약 대기)"
    )
    @ExceptionDescription(CREATE_RESERVATION)
    @PostMapping
    public BaseResponse<ReservationIdResponse> createReservation(
            @RequestBody @Valid final CreateReservationRequest request,
            @Parameter(hidden = true) @UserId final Long ownerId
    ) {
        return BaseResponse.ok(ReservationIdResponse.of(
                reservationService.createReservation(request, ownerId)
        ));
    }

    @Operation(
            summary = "예약 견적서 조회",
            description = "사장님이 작성한 예약 견적서를 조회합니다. 사장님, 일반 유저 모두 조회 가능합니다."
    )
    @ExceptionDescription(GET_RESERVATION)
    @GetMapping("/{reservationId}")
    public BaseResponse<ReservationResponse> getReservation(
            @Parameter(description = "예약 ID", example = "1") @PathVariable final Long reservationId,
            @Parameter(hidden = true) @UserId final Long userId
    ) {
        return BaseResponse.ok(reservationService.getReservation(reservationId, userId));
    }

    @Operation(
            summary = "예약 견적서 수정",
            description = "사장님이 작성한 예약 견적서를 수정합니다. 사장님, 일반 유저 모두 수정 가능합니다."
    )
    @ExceptionDescription(UPDATE_RESERVATION)
    @PutMapping("/{reservationId}")
    public BaseResponse<Void> updateReservation(
            @Parameter(description = "예약 ID", example = "1") @PathVariable final Long reservationId,
            @RequestBody @Valid final UpdateReservationRequest request,
            @Parameter(hidden = true) @UserId final Long userId
    ) {
        reservationService.updateReservation(reservationId, request, userId);
        return BaseResponse.ok(null);
    }

    @Operation(
            summary = "예약 상태 변경",
            description = "요청에 따라 예약 상태를 변경합니다." +
                    "예약 상태 변경 순서: 예약 대기 -> 예약 확정 요청 -> 예약 확정 -> 예약 취소 요청 -> 예약 취소"
    )
    @ExceptionDescription(UPDATE_RESERVATION_STATUS)
    @PatchMapping("{reservationId}/status")
    public BaseResponse<ReservationStatusResponse> updateReservationStatus(
            @Parameter(description = "예약 ID", example = "1") @PathVariable final Long reservationId,
            @RequestBody @Valid final UpdateReservationStatusRequest request,
            @Parameter(hidden = true) @UserId final Long userId
    ) {
        return BaseResponse.ok(
                reservationService.updateReservationStatus(reservationId, request, userId)
        );
    }

    @Operation(
            summary = "예약 상태 조회",
            description = "예약 ID로 예약 상태를 조회합니다."
    )
    @ExceptionDescription(GET_RESERVATION_STATUS)
    @GetMapping("/{reservationId}/status")
    public BaseResponse<ReservationStatusResponse> getReservationStatus(
            @Parameter(description = "예약 ID", example = "1") @PathVariable final Long reservationId,
            @Parameter(hidden = true) @UserId final Long userId
    ) {
        return BaseResponse.ok(
                reservationService.getReservationStatus(reservationId, userId)
        );
    }
}
