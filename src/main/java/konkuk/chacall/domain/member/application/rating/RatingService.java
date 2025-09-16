package konkuk.chacall.domain.member.application.rating;

import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.member.domain.repository.RatingRepository;
import konkuk.chacall.domain.member.presentation.dto.request.RegisterRatingRequest;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RatingService {

    private final RatingRepository ratingRepository;
    private final FoodTruckRepository foodTruckRepository;

    @Transactional
    public void registerRatings(RegisterRatingRequest request, Long memberId) {
        // 유효한 평점 값인지 확인

        // 푸드트럭이 존재하는지 확인
        FoodTruck foodTruck = foodTruckRepository.findById(request.foodTruckId())
                .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_TRUCK_NOT_FOUND));

        // 로그인한 사용자가 해당 푸드트럭에 대해 아직 평점을 등록하지 않은 경우에만 평점 등록
        ratingRepository.findByMemberIdAndFoodTruckIdAndIsRatedFalse(memberId, request.foodTruckId())
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
}
