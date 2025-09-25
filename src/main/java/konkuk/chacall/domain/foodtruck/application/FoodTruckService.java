package konkuk.chacall.domain.foodtruck.application;

import konkuk.chacall.domain.foodtruck.application.command.FoodTruckCommandService;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.ApproveFoodTruckStatusRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.FoodTruckNameDuplicateCheckRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.FoodTruckSearchRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.FoodTruckNameDuplicateCheckResponse;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.FoodTruckResponse;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FoodTruckService {

    private final FoodTruckCommandService foodTruckCommandService;

    public CursorPagingResponse<FoodTruckResponse> getFoodTrucks(FoodTruckSearchRequest request) {
        return foodTruckCommandService.getFoodTrucks(request);
    }

    public FoodTruckNameDuplicateCheckResponse isNameDuplicated(FoodTruckNameDuplicateCheckRequest request) {
        return FoodTruckNameDuplicateCheckResponse.of(
                foodTruckCommandService.isNameDuplicated(request.name()));
    }

    @Transactional
    public void approveFoodTruckStatus(Long foodTruckId, ApproveFoodTruckStatusRequest request) {
        foodTruckCommandService.approveFoodTruckStatus(foodTruckId, request);
    }
}
