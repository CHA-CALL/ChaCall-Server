package konkuk.chacall.domain.member.application;

import konkuk.chacall.domain.member.application.foodtruck.SavedFoodTruckService;
import konkuk.chacall.domain.member.presentation.dto.request.UpdateFoodTruckSaveStatusRequest;
import konkuk.chacall.domain.member.presentation.dto.response.SavedFoodTruckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final SavedFoodTruckService savedFoodTruckService;

    public SavedFoodTruckResponse updateFoodTruckSaveStatus(UpdateFoodTruckSaveStatusRequest request, Long foodTruckId, Long userId) {
        return savedFoodTruckService.updateFoodTruckSaveStatus(request, foodTruckId, userId);
    }
}
