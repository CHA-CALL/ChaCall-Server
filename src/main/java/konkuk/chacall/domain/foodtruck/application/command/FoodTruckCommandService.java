package konkuk.chacall.domain.foodtruck.application.command;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.FoodTruckSearchRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.FoodTruckResponse;
import konkuk.chacall.domain.member.domain.repository.SavedFoodTruckRepository;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FoodTruckCommandService {

    private final FoodTruckRepository foodTruckRepository;
    private final SavedFoodTruckRepository savedFoodTruckRepository;

    public CursorPagingResponse<FoodTruckResponse> getFoodTrucks(Long memberId, FoodTruckSearchRequest request) {

        Slice<FoodTruck> foodTruckSlice = foodTruckRepository.getFoodTrucks(request);
        List<FoodTruck> content = foodTruckSlice.getContent();

        // 이 페이지의 트럭 ID 목록
        List<Long> foodTruckIds = content.stream()
                .map(FoodTruck::getFoodTruckId)
                .toList();

        Set<Long> savedFoodTruckIds = foodTruckIds.isEmpty() ? Set.of() : savedFoodTruckRepository.findSavedTruckIdsIn(memberId, foodTruckIds);

        List<FoodTruckResponse> foodTrucks = content.stream()
                .map(ft -> FoodTruckResponse.of(ft, savedFoodTruckIds.contains(ft.getFoodTruckId())))
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
