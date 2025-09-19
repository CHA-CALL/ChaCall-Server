package konkuk.chacall.domain.reservation.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.chacall.domain.reservation.application.ReservationService;
import konkuk.chacall.domain.reservation.presentation.dto.request.CreateReservationRequest;
import konkuk.chacall.domain.reservation.presentation.dto.response.ReservationIdResponse;
import konkuk.chacall.global.common.annotation.UserId;
import konkuk.chacall.global.common.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping
    public BaseResponse<ReservationIdResponse> createReservation(
            @RequestBody @Valid final CreateReservationRequest request,
            @Parameter(hidden = true) @UserId final Long ownerId
    ) {
        return BaseResponse.ok(ReservationIdResponse.of(
                reservationService.createReservation(request, ownerId)
        ));
    }
}
