package konkuk.chacall.domain.member.application.rating;

import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.member.domain.repository.RatingRepository;
import konkuk.chacall.domain.member.presentation.dto.request.RegisterRatingRequest;
import konkuk.chacall.domain.member.presentation.dto.response.ReservationForRatingResponse;
import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.repository.ReservationRepository;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final FoodTruckRepository foodTruckRepository;
    private final ReservationRepository reservationRepository;

    public void registerRatings(RegisterRatingRequest request, User member) {
        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        reservation.validateCanBeRatedBy(member);

        // 푸드트럭이 존재하는지 확인
        FoodTruck foodTruck = foodTruckRepository.findById(request.foodTruckId())
                .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_TRUCK_NOT_FOUND));

        // 로그인한 사용자가 해당 푸드트럭에 대해 아직 평점을 등록하지 않은 경우에만 평점 등록
        ratingRepository.findByMemberAndFoodTruckAndIsRatedFalse(member, foodTruck)
                .ifPresentOrElse(rating -> {
                    double rate = Double.parseDouble(request.rating());
                    // 평점 등록
                    rating.registerRating(rate);

                    // 푸드트럭의 평균 평점 업데이트
                    foodTruck.updateAverageRating(rate);
                }, () -> {
                    throw new BusinessException(ErrorCode.RATING_NOT_FOUND);
                });
    }

    public ReservationForRatingResponse getReservationsForRating(User member) {
        var reservationForRatings = ratingRepository.findAllByMemberAndIsRatedFalse(member).stream()
                .map(rating -> ReservationForRatingResponse.ReservationForRating.of(
                        rating.getFoodTruck(), rating.getReservation()
                ))
                .toList();

        return new ReservationForRatingResponse(reservationForRatings);
    }
}
