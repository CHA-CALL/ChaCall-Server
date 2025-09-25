package konkuk.chacall.domain.foodtruck.application;

import konkuk.chacall.domain.foodtruck.application.duplicatecheck.FoodTruckNameDuplicateCheckService;
import konkuk.chacall.domain.foodtruck.application.search.FoodTruckSearchService;
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

    private final FoodTruckSearchService foodTruckSearchService;
    private final FoodTruckNameDuplicateCheckService foodTruckNameDuplicateCheckService;

    public CursorPagingResponse<FoodTruckResponse> getFoodTrucks(FoodTruckSearchRequest request) {
        return foodTruckSearchService.getFoodTrucks(request);
    }

    public FoodTruckNameDuplicateCheckResponse isNameDuplicated(FoodTruckNameDuplicateCheckRequest request) {
        return FoodTruckNameDuplicateCheckResponse.of(
                foodTruckNameDuplicateCheckService.isNameDuplicated(request.name()));
    }
}
