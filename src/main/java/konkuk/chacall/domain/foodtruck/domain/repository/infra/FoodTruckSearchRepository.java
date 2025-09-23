package konkuk.chacall.domain.foodtruck.domain.repository.infra;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.FoodTruckSearchRequest;
import org.springframework.data.domain.Slice;

public interface FoodTruckSearchRepository {
    Slice<FoodTruck> getFoodTrucks(FoodTruckSearchRequest request);
}
