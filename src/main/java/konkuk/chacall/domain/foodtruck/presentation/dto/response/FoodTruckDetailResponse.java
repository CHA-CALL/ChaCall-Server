package konkuk.chacall.domain.foodtruck.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.foodtruck.domain.model.AvailableDate;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruckServiceArea;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckInfo;
import konkuk.chacall.domain.region.domain.model.Region;
import konkuk.chacall.domain.region.presentation.dto.response.RegionResponse;

import java.util.List;

public record FoodTruckDetailResponse(
        @Schema(description = "푸드트럭 식별자", example = "1")
        Long foodTruckId,
        @Schema(description = "푸드트럭 이름", example = "푸드트럭")
        String name,
        @Schema(description = "푸드트럭 설명", example = "맛있는 푸드트럭입니다.")
        String description,
        @Schema(description = "푸드트럭 전화번호", example = "010-1234-5678")
        String phoneNumber,
        @Schema(description = "푸드트럭 활동 시간", example = "09:00-20:00")
        String activeTime,
        @Schema(description = "시간 협의 필요 여부", example = "false")
        Boolean timeDiscussRequired,
        @Schema(description = "호출 가능 지역", example = "서울 광진구, 서울 강남구, 서울 영등포구")
        String serviceAreas,
        @Schema(description = "호출 가능 지역 정보 리스트")
        List<RegionResponse> regionCodes,
        @Schema(description = "푸드트럭 메뉴 카테고리 (라벨 리스트)", example = "[\"한식\",\"분식\"]")
        List<String> menuCategories,
        @Schema(description = "푸드트럭 제공 가능 수량", example = "200인분 미만")
        String availableQuantity,
        @Schema(description = "전기 사용 필요 여부", example = "필요")
        String needElectricity,
        @Schema(description = "결제 방법", example = "무관")
        String paymentMethod,
        @Schema(description = "푸드트럭 제공 가능 날짜 리스트", example = "[\"2025-10-01 ~ 2025-10-10\",\"2025-11-01 ~ 2025-11-10\"]")
        List<String> availableDates,
        @Schema(description = "푸드트럭 사진 URL 리스트", example = "[\"http://image.png\",\"http://image2.png\",\"http://image3.png\"]")
        List<String> photoUrl,
        @Schema(description = "운영 정보", example = "운영정보")
        String operatingInfo,
        @Schema(description = "추가 옵션 정보", example = "안녕하세요")
        String option,
        @Schema(description = "푸드트럭 평균 평점", example = "4.5")
        Double averageRating,
        @Schema(description = "현재 사용자가 저장한 푸드트럭인지 여부", example = "true")
        Boolean isSaved
) {
    public static FoodTruckDetailResponse from(FoodTruck foodTruck, List<FoodTruckServiceArea> serviceAreas, List<AvailableDate> availableDates, Boolean isSaved) {
        FoodTruckInfo foodTruckInfo = foodTruck.getFoodTruckInfo();
        List<RegionResponse> regionResponseList = serviceAreas.stream().map(FoodTruckServiceArea::getRegion).map(region -> RegionResponse.of(region.getName(), region.getRegionId(), region.getRegionCode())).toList();
        return new FoodTruckDetailResponse(
                foodTruck.getFoodTruckId(),
                foodTruckInfo.getName(),
                foodTruckInfo.getDescription(),
                foodTruckInfo.getPhoneNumber(),
                foodTruckInfo.getActiveTime(),
                foodTruckInfo.getTimeDiscussRequired(),
                foodTruck.getServiceAreas(serviceAreas),
                regionResponseList,
                foodTruckInfo.getMenuCategoryList().getMenuCategoryLabelList(),
                foodTruckInfo.getAvailableQuantity().getValue(),
                foodTruckInfo.getNeedElectricity().getValue(),
                foodTruckInfo.getPaymentMethod().getValue(),
                foodTruck.getAvailableDates(availableDates),
                foodTruckInfo.getFoodTruckPhotoList().getUrls(),
                foodTruckInfo.getOperatingInfo(),
                foodTruckInfo.getOption(),
                foodTruck.getRatingInfo().getAverageRating(),
                isSaved
        );
    }
}
