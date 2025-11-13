package konkuk.chacall.domain.foodtruck.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;

public record FoodTruckTopRateResponse(
        @Schema(description = "푸드트럭 식별자", example = "1")
        Long foodTruckId,
        @Schema(description = "푸드트럭 이름", example = "푸드트럭")
        String name,
        @Schema(description = "푸드트럭 대표 사진 URL", example = "http://image.png")
        String photoUrl,
        @Schema(description = "푸드트럭 평균 평점", example = "4.5")
        Double averageRating
) {
    public static FoodTruckTopRateResponse from(FoodTruck foodTruck) {
        return new FoodTruckTopRateResponse(
                foodTruck.getFoodTruckId(),
                foodTruck.getFoodTruckInfo().getName(),
                foodTruck.getFoodTruckInfo().getFoodTruckPhotoList().getMainPhotoUrl(),
                foodTruck.getRatingInfo().getAverageRating()
        );
    }
}
