package konkuk.chacall.domain.owner.application.myFoodTruck;

import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.FoodTruckServiceArea;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckServiceAreaRepository;
import konkuk.chacall.domain.owner.presentation.dto.response.MyFoodTruckResponse;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MyFoodTruckService {

    private final FoodTruckRepository foodTruckRepository;
    private final FoodTruckServiceAreaRepository foodTruckServiceAreaRepository;

    public CursorPagingResponse<MyFoodTruckResponse> getMyFoodTrucks(CursorPagingRequest request, Long ownerId) {
        // 1. 커서 기반으로 푸드트럭 Slice 조회
        Slice<FoodTruck> foodTruckSlice = findFoodTrucks(ownerId, request.cursor(), request.size());
        List<FoodTruck> foodTrucks = foodTruckSlice.getContent();

        // 2. 호출 가능 지역 정보 Map 조회
        Map<Long, List<FoodTruckServiceArea>> serviceAreaMap = getServiceAreaMap(foodTrucks);

        // 3. DTO 리스트 생성
        List<MyFoodTruckResponse> responses = mapToMyFoodTruckResponse(foodTrucks, serviceAreaMap);

        return CursorPagingResponse.of(responses, MyFoodTruckResponse::foodTruckId, foodTruckSlice.hasNext());
    }

    /**
     * 커서 기반으로 페이징된 푸드트럭 목록 조회
     */
    private Slice<FoodTruck> findFoodTrucks(Long ownerId, Long lastCursor, int pageSize) {
        return foodTruckRepository.findByOwnerUserIdWithCursor(ownerId, lastCursor, PageRequest.of(0, pageSize));
    }

    /**
     * 푸드트럭 목록에 포함된 호출 가능 지역 정보 Map 조회
     */
    private Map<Long, List<FoodTruckServiceArea>> getServiceAreaMap(List<FoodTruck> foodTrucks) {
        List<Long> foodTruckIds = foodTrucks.stream()
                .map(FoodTruck::getFoodTruckId)
                .toList();

        List<FoodTruckServiceArea> serviceAreas = foodTruckServiceAreaRepository
                .findAllWithRegionByFoodTruckIdIn(foodTruckIds);

        return serviceAreas.stream()
                .collect(Collectors.groupingBy(sa -> sa.getFoodTruck().getFoodTruckId()));
    }

    private List<MyFoodTruckResponse> mapToMyFoodTruckResponse(List<FoodTruck> foodTrucks, Map<Long, List<FoodTruckServiceArea>> serviceAreaMap) {
        return foodTrucks.stream()
                .map(foodTruck -> {
                    List<FoodTruckServiceArea> serviceAreas = serviceAreaMap.getOrDefault(foodTruck.getFoodTruckId(), List.of());
                    return MyFoodTruckResponse.of(foodTruck, serviceAreas);
                })
                .toList();
    }


}
