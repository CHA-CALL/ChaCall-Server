package konkuk.chacall.domain.foodtruck.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record FoodTruckNameDuplicateCheckResponse(
        @Schema(description = "중복 여부", example = "true")
        boolean duplicated
) {
    public static FoodTruckNameDuplicateCheckResponse of(boolean duplicated) {
        return new FoodTruckNameDuplicateCheckResponse(duplicated);
    }
}
