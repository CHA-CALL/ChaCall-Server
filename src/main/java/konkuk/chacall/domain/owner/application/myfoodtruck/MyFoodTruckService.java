package konkuk.chacall.domain.owner.application.myfoodtruck;

import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.FoodTruckServiceArea;
import konkuk.chacall.domain.foodtruck.domain.repository.AvailableDateRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckServiceAreaRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.MenuRepository;
import konkuk.chacall.domain.member.domain.repository.RatingRepository;
import konkuk.chacall.domain.member.domain.repository.SavedFoodTruckRepository;
import konkuk.chacall.domain.owner.presentation.dto.response.MyFoodTruckResponse;
import konkuk.chacall.domain.reservation.domain.repository.ReservationRepository;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MyFoodTruckService {

    private final FoodTruckRepository foodTruckRepository;
    private final FoodTruckServiceAreaRepository foodTruckServiceAreaRepository;
    private final MenuRepository menuRepository;
    private final ReservationRepository reservationRepository;
    private final SavedFoodTruckRepository savedFoodTruckRepository;
    private final AvailableDateRepository availableDateRepository;
    private final RatingRepository ratingRepository;

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

    public void deleteMyFoodTruck(Long ownerId, Long foodTruckId) {
        // 푸드트럭 조회 및 소유권 확인
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FOOD_TRUCK_NOT_FOUND));

        if (!foodTruck.isOwnedBy(ownerId)) {
            throw new BusinessException(ErrorCode.FOOD_TRUCK_NOT_OWNED);
        }

        // 푸드트럭 호출 가능 지역 삭제
        foodTruckServiceAreaRepository.deleteAllByFoodTruckId(foodTruckId);

        // 푸드트럭 메뉴 삭제
        menuRepository.deleteAllByFoodTruckId(foodTruckId);

        // 푸드트럭 가능한 일정대 삭제
        availableDateRepository.deleteAllByFoodTruckId(foodTruckId);

        // 푸드트럭 관련 평점 삭제
        ratingRepository.deleteAllByFoodTruckId(foodTruckId);

        // 푸드트럭 관련 예약 삭제
        reservationRepository.deleteAllByFoodTruckId(foodTruckId);

        // 푸드트럭 저장한 푸드트럭 삭제
        savedFoodTruckRepository.deleteAllByFoodTruckId(foodTruckId);

        // 푸드트럭 삭제
        foodTruckRepository.delete(foodTruck);
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
