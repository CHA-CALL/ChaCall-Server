package konkuk.chacall.domain.foodtruck.application.command;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.FoodTruckSearchRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.FoodTruckResponse;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodTruckCommandService {

    private final FoodTruckRepository foodTruckRepository;

    public CursorPagingResponse<FoodTruckResponse> getFoodTrucks(FoodTruckSearchRequest request) {

        Slice<FoodTruck> foodTruckSlice = foodTruckRepository.getFoodTrucks(request);
        List<FoodTruckResponse> foodTrucks = foodTruckSlice.getContent().stream()
                .map(FoodTruckResponse::of)
                .toList();

        return CursorPagingResponse.of(
                foodTrucks,
                FoodTruckResponse::foodTruckId,
                foodTruckSlice.hasNext()
        );
    }

    public boolean isNameDuplicated(String name) {
        return foodTruckRepository.existsByName(name);
    }
}
