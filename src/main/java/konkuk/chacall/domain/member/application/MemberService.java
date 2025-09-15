package konkuk.chacall.domain.member.application;

import jakarta.validation.Valid;
import konkuk.chacall.domain.member.application.foodtruck.SavedFoodTruckService;
import konkuk.chacall.domain.member.application.rating.RatingService;
import konkuk.chacall.domain.member.presentation.dto.request.RegisterRatingRequest;
import konkuk.chacall.domain.member.presentation.dto.request.UpdateFoodTruckSaveStatusRequest;
import konkuk.chacall.domain.member.presentation.dto.response.SavedFoodTruckStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final SavedFoodTruckService savedFoodTruckService;
    private final RatingService ratingService;

    public SavedFoodTruckStatusResponse updateFoodTruckSaveStatus(UpdateFoodTruckSaveStatusRequest request, Long foodTruckId, Long memberId) {
        return savedFoodTruckService.updateFoodTruckSaveStatus(request, foodTruckId, memberId);
    }

    public void registerRatings(RegisterRatingRequest request, Long memberId) {
        ratingService.registerRatings(request, memberId);
    }
}
