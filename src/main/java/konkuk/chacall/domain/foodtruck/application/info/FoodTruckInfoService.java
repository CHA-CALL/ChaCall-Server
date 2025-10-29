package konkuk.chacall.domain.foodtruck.application.info;

import konkuk.chacall.domain.foodtruck.domain.model.AvailableDate;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruckServiceArea;
import konkuk.chacall.domain.foodtruck.domain.repository.AvailableDateRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckServiceAreaRepository;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.DateRangeRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.FoodTruckSearchRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.UpdateFoodTruckInfoRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.FoodTruckDetailResponse;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.FoodTruckResponse;
import konkuk.chacall.domain.member.domain.repository.SavedFoodTruckRepository;
import konkuk.chacall.domain.region.domain.model.Region;
import konkuk.chacall.domain.region.domain.repository.RegionRepository;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodTruckInfoService {

    private final FoodTruckRepository foodTruckRepository;
    private final SavedFoodTruckRepository savedFoodTruckRepository;
    private final FoodTruckServiceAreaRepository foodTruckServiceAreaRepository;
    private final AvailableDateRepository availableDateRepository;
    private final RegionRepository regionRepository;

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
        return foodTruckRepository.existsByFoodTruckInfo_Name(name);
    }

    public Long updateMyFoodTruckInfo(User owner, Long foodTruckId, UpdateFoodTruckInfoRequest request) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FOOD_TRUCK_NOT_FOUND));

        // 푸드트럭 소유주/승인 상태 검증
        foodTruck.validateOwner(owner.getUserId());
        foodTruck.validateApprovedStatus();

        // 푸드트럭 정보 업데이트
        foodTruck.updateFoodTruckInfo(request.name(), request.description(), request.phoneNumber(), request.activeTime(), request.timeDiscussRequired(), request.photoUrls(),
                request.menuCategories(), request.availableQuantity(), request.needElectricity(), request.paymentMethod(), request.operatingInfo(), request.option());

        // 푸드트럭 노출 상태 변경 허용
        foodTruck.permitChangeViewStatus();

        // 서비스 가능 지역 동기화
        syncServiceAreas(foodTruck, request.foodTruckServiceAreas());

        // 운영 가능 날짜 동기화
        syncAvailableDates(foodTruck, request.availableDates());

        return foodTruck.getFoodTruckId();
    }

    private void syncAvailableDates(FoodTruck foodTruck, List<DateRangeRequest> dateRangeRequests) {
        // 기존 운영 가능 날짜 삭제
        availableDateRepository.deleteAllByFoodTruckId(foodTruck.getFoodTruckId());

        // 새로운 운영 가능 날짜 추가
        List<AvailableDate> newAvailableDates = dateRangeRequests.stream()
                .map(request -> AvailableDate.createAvailableDate(request.startDate(), request.endDate(), foodTruck))
                .toList();

        availableDateRepository.saveAll(newAvailableDates);
    }

    private void syncServiceAreas(FoodTruck foodTruck, Set<Long> requestedRegionIds) {
        Set<Long> currentRegionIds = foodTruckServiceAreaRepository.findAllByFoodTruckId(foodTruck.getFoodTruckId()).stream()
                .map(FoodTruckServiceArea::getRegion)
                .map(Region::getRegionId)
                .collect(Collectors.toSet());

        // 추가할 지역
        Set<Long> regionsToAdd = new HashSet<>(requestedRegionIds);
        regionsToAdd.removeAll(currentRegionIds); // 현재 지역에서 없는 것들만 남김

        // 제거할 지역
        Set<Long> regionsToRemove = new HashSet<>(currentRegionIds);
        regionsToRemove.removeAll(requestedRegionIds); // 요청된 지역에서 없는 것들만 남김

        // 추가
        if(!regionsToAdd.isEmpty()) {
            List<FoodTruckServiceArea> serviceAreasToAdd = regionsToAdd.stream()
                    .map(regionId -> FoodTruckServiceArea.createFoodTruckServiceArea(regionRepository.findById(regionId)
                            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.REGION_NOT_FOUND)), foodTruck))
                    .toList();

            foodTruckServiceAreaRepository.saveAll(serviceAreasToAdd);
        }

        // 제거
        if(!regionsToRemove.isEmpty()) {
            List<FoodTruckServiceArea> serviceAreasToRemove = regionsToRemove.stream()
                    .map(regionId -> foodTruckServiceAreaRepository.findByFoodTruckIdAndRegionId(foodTruck.getFoodTruckId(), regionId)
                            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FOOD_TRUCK_SERVICE_AREA_NOT_FOUND)))
                    .toList();

            foodTruckServiceAreaRepository.deleteAll(serviceAreasToRemove);
        }
    }

    public FoodTruckDetailResponse getFoodTruckDetails(User member, Long foodTruckId) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FOOD_TRUCK_NOT_FOUND));

        switch(member.getRole()) {
            case MEMBER -> foodTruck.validateViewableStatusForMember();
            case OWNER -> foodTruck.validateOwner(member.getUserId());
            case ADMIN -> {}
        }

        List<FoodTruckServiceArea> foodTruckServiceAreas = foodTruckServiceAreaRepository.findAllByFoodTruckId(foodTruckId);
        List<AvailableDate> availableDates = availableDateRepository.findAllByFoodTruckId(foodTruckId);

        boolean isSaved = savedFoodTruckRepository.existsByMemberIdAndFoodTruckId(member.getUserId(), foodTruckId);

        return FoodTruckDetailResponse.from(foodTruck, foodTruckServiceAreas, availableDates, isSaved);
    }
}
