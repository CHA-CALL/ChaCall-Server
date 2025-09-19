package konkuk.chacall.domain.reservation.application.reservationinfo;

import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.repository.ReservationRepository;
import konkuk.chacall.domain.reservation.presentation.dto.request.CreateReservationRequest;
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

    public Long createReservation(CreateReservationRequest request, User owner) {
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
                foodTruck);

        return reservationRepository.save(reservation).getReservationId();
    }
}
