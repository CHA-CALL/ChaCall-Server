package konkuk.chacall.domain.foodtruck.application;

import konkuk.chacall.domain.foodtruck.application.search.FoodTruckSearchService;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.FoodTruckSearchRequest;
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

    public CursorPagingResponse<FoodTruckResponse> getFoodTrucks(FoodTruckSearchRequest request) {
        return foodTruckSearchService.getFoodTrucks(request);
    }
}
