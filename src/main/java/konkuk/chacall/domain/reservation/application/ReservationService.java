package konkuk.chacall.domain.reservation.application;

import konkuk.chacall.domain.member.application.validator.MemberValidator;
import konkuk.chacall.domain.owner.application.validator.OwnerValidator;
import konkuk.chacall.domain.reservation.application.info.ReservationInfoService;
import konkuk.chacall.domain.reservation.application.status.ReservationStatusService;
import konkuk.chacall.domain.reservation.presentation.dto.request.CreateReservationRequest;
import konkuk.chacall.domain.reservation.presentation.dto.request.UpdateReservationRequest;
import konkuk.chacall.domain.reservation.presentation.dto.request.UpdateReservationStatusRequest;
import konkuk.chacall.domain.reservation.presentation.dto.response.ReservationResponse;
import konkuk.chacall.domain.reservation.presentation.dto.response.ReservationStatusResponse;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationInfoService reservationInfoService;
    private final ReservationStatusService reservationStatusService;

    private final OwnerValidator ownerValidator;
    private final MemberValidator memberValidator;

    @Transactional
    public Long createReservation(CreateReservationRequest request, Long ownerId) {
        User owner = ownerValidator.validateAndGetOwner(ownerId);
        User member = memberValidator.validateAndGetMember(request.reservationUserId());

        return reservationInfoService.createReservation(request, owner, member);
    }

    public ReservationResponse getReservation(Long reservationId, Long userId) {
        User user = memberValidator.validateAndGetMember(userId);

        return reservationInfoService.getReservation(reservationId, user);
    }

    @Transactional
    public Long updateReservation(Long reservationId, UpdateReservationRequest request, Long userId) {
        User user = memberValidator.validateAndGetMember(userId);

        return reservationInfoService.updateReservation(reservationId, request, user);
    }

    @Transactional
    public ReservationStatusResponse updateReservationStatus(Long reservationId, UpdateReservationStatusRequest request, Long userId) {
        switch(request.reservationStatus()) {
            case CONFIRMED_REQUESTED -> {
                User member = memberValidator.validateAndGetMember(userId);
                return reservationStatusService.updateReservationStatusToConfirmedRequested(reservationId, request, member);
            }
            case CONFIRMED -> {
                User owner = ownerValidator.validateAndGetOwner(userId);
                return reservationStatusService.updateReservationStatusToConfirmed(reservationId, request, owner);
            }
            case CANCELLED, CANCELLED_REQUESTED -> {
                User user = memberValidator.validateAndGetMember(userId);
                return reservationStatusService.updateReservationStatusToCancelled(reservationId, request, user);
            }
            default -> throw new BusinessException(ErrorCode.INVALID_RESERVATION_STATUS);
        }
    }

    public ReservationStatusResponse getReservationStatus(Long reservationId, Long userId) {
        User user = memberValidator.validateAndGetMember(userId);
        return reservationStatusService.getReservationStatus(reservationId, user);
    }
}
