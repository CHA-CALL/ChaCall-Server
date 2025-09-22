package konkuk.chacall.domain.foodtruck.domain.repository.querydsl;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.FoodTruckSearchRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface FoodTruckSearchRepository {
    Slice<FoodTruck> getFoodTrucks(FoodTruckSearchRequest request);
}
