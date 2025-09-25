package konkuk.chacall.domain.foodtruck.application.command;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.ApproveFoodTruckStatusRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.FoodTruckSearchRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.FoodTruckResponse;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
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
        return foodTruckRepository.existsByNameIgnoreCase(name);
    }

    // 운영자 가 사용할 기능 - 소유권 검증 필요 X
    public void approveFoodTruckStatus(Long foodTruckId, ApproveFoodTruckStatusRequest request) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FOOD_TRUCK_NOT_FOUND));

        foodTruck.changeFoodTruckStatus(request.status());
    }
}
