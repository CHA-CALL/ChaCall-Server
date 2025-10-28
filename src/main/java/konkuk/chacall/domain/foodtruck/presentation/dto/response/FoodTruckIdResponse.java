package konkuk.chacall.domain.foodtruck.presentation.dto.response;

public record FoodTruckIdResponse(
        Long foodTruckId
) {
    public static FoodTruckIdResponse of(Long foodTruckId) {
        return new FoodTruckIdResponse(foodTruckId);
    }
}
