package konkuk.chacall.domain.reservation.application.status;

import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.repository.ReservationRepository;
import konkuk.chacall.domain.reservation.presentation.dto.request.UpdateReservationStatusRequest;
import konkuk.chacall.domain.reservation.presentation.dto.response.ReservationStatusResponse;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationStatusService {

    private final ReservationRepository reservationRepository;

    public ReservationStatusResponse updateReservationStatusToConfirmedRequested(Long reservationId, UpdateReservationStatusRequest request, User member) {

        Reservation reservation = findReservation(reservationId);

        // CONFIRMED_REQUESTED는 예약자만 가능
        reservation.validateReservedBy(member.getUserId());

        updateReservationStatus(reservation, request);
        return new ReservationStatusResponse(reservation.getReservationStatus());
    }

    public ReservationStatusResponse updateReservationStatusToConfirmed(Long reservationId, UpdateReservationStatusRequest request, User owner) {

        Reservation reservation = findReservation(reservationId);

        // CONFIRMED는 사장님만 가능
        reservation.validateFoodTruckOwner(owner.getUserId());

        updateReservationStatus(reservation, request);
        return new ReservationStatusResponse(reservation.getReservationStatus());
    }

    public ReservationStatusResponse updateReservationStatusToCancelled(Long reservationId, UpdateReservationStatusRequest request, User user) {

        Reservation reservation = findReservation(reservationId);

        // CANCELLED_REQUESTED, CONFIRMED 모두 예약자, 사장님 모두 가능
        reservation.validateAccessibleBy(user.getUserId());

        updateReservationStatus(reservation, request);
        return new ReservationStatusResponse(reservation.getReservationStatus());
    }

    public ReservationStatusResponse getReservationStatus(Long reservationId, User user) {
        Reservation reservation = findReservation(reservationId);

        // 예약 소유자 또는 푸드트럭 소유자인지 확인
        reservation.validateAccessibleBy(user.getUserId());

        return new ReservationStatusResponse(reservation.getReservationStatus());
    }

    // == 공통 Private Methods == //
    private Reservation findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
                () -> new EntityNotFoundException(ErrorCode.RESERVATION_NOT_FOUND)
        );
    }

    private void updateReservationStatus(Reservation reservation, UpdateReservationStatusRequest request) {
        reservation.updateStatus(request.reservationStatus());
        reservationRepository.save(reservation);
    }


}
