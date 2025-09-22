package konkuk.chacall.domain.member.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;

public record SavedFoodTruckResponse(
        @Schema(description = "푸드트럭 식별자", example = "1")
        Long foodTruckId,
        @Schema(description = "푸드트럭 이름", example = "푸드트럭")
        String name,
        @Schema(description = "푸드트럭 대표 사진 URL", example = "http://image.png")
        String photoUrl,
        @Schema(description = "푸드트럭 설명", example = "맛있는 푸드트럭입니다.")
        String description,
        @Schema(description = "푸드트럭 평균 평점", example = "4.5")
        Double averageRating,
        @Schema(description = "푸드트럭 평점 수", example = "100")
        Integer ratingCount
) {
    public static SavedFoodTruckResponse of(FoodTruck foodTruck) {
        return new SavedFoodTruckResponse(
                foodTruck.getFoodTruckId(),
                foodTruck.getName(),
                foodTruck.getFoodTruckPhotoList().getMainPhotoUrl(), // 대표 사진 (첫 번째 사진)
                foodTruck.getDescription(),
                foodTruck.getRatingInfo().getAverageRating(),
                foodTruck.getRatingInfo().getRatingCount()
        );
    }
}
