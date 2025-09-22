package konkuk.chacall.domain.reservation.application.info;

import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.repository.ReservationRepository;
import konkuk.chacall.domain.reservation.presentation.dto.request.CreateReservationRequest;
import konkuk.chacall.domain.reservation.presentation.dto.request.UpdateReservationRequest;
import konkuk.chacall.domain.reservation.presentation.dto.response.ReservationResponse;
import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationInfoService {

    private final FoodTruckRepository foodTruckRepository;
    private final ReservationRepository reservationRepository;

    public Long createReservation(CreateReservationRequest request, User owner, User member) {
        FoodTruck foodTruck = foodTruckRepository.findById(request.foodTruckId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FOOD_TRUCK_NOT_FOUND));

        Reservation reservation = Reservation.create(
                request.address(),
                request.detailAddress(),
                request.reservationDates(),
                request.operationHour(),
                request.menu(),
                request.deposit(),
                request.isUseElectricity(),
                request.etcRequest(),
                owner,
                member,
                foodTruck);

        return reservationRepository.save(reservation).getReservationId();
    }

    public ReservationResponse getReservation(Long reservationId, User user) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.RESERVATION_NOT_FOUND));

        // 예약 소유자 또는 푸드트럭 소유자인지 확인
        if(user.getRole().equals(Role.OWNER)) {
            reservation.validateFoodTruckOwner(user.getUserId());
        } else {
            reservation.validateReservedBy(user.getUserId());
        }

        return ReservationResponse.of(reservation);
    }

    public void updateReservation(Long reservationId, UpdateReservationRequest request, User user) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.RESERVATION_NOT_FOUND));

        // 예약 소유자 또는 푸드트럭 소유자인지 확인
        if(user.getRole().equals(Role.OWNER)) {
            reservation.validateFoodTruckOwner(user.getUserId());
        } else {
            reservation.validateReservedBy(user.getUserId());
        }

        reservation.update(
                request.address(),
                request.detailAddress(),
                request.reservationDates(),
                request.operationHour(),
                request.menu(),
                request.deposit(),
                request.isUseElectricity(),
                request.etcRequest()
        );
    }
}
