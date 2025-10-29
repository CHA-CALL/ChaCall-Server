package konkuk.chacall.domain.user.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;

import java.util.List;

public record FoodTruckForAdminResponse(
        @Schema(description = "푸드트럭 ID", example = "1")
        Long foodTruckId,
        @Schema(description = "푸드트럭 이름", example = "차콜 푸드트럭")
        String foodTruckName,
        @Schema(description = "푸드트럭 사장님 이름", example = "홍길동")
        String ownerName,
        @Schema(description = "푸드트럭 상태", example = "승인 대기")
        String foodTruckStatus,
        @Schema(description = "푸드트럭 서류 URL 목록 (사업자 등록증 포함)", example = "[\"https://cdn.chacall.com/foodtrucks/osori/doc1.jpg\", \"https://cdn.chacall.com/foodtrucks/osori/doc2.jpg\", \"https://cdn.chacall.com/foodtrucks/osori/doc3.jpg\", \"https://cdn.chacall.com/foodtrucks/osori/doc4.jpg\", \"https://cdn.chacall.com/foodtrucks/osori/doc5.jpg\"]")
        List<String> foodTruckDocumentUrls
) {
    public static FoodTruckForAdminResponse from(FoodTruck foodTruck, List<String> foodTruckDocumentUrls) {
        return new FoodTruckForAdminResponse(
                foodTruck.getFoodTruckId(),
                foodTruck.getFoodTruckInfo().getName(),
                foodTruck.getOwner().getName(),
                foodTruck.getFoodTruckStatus().getDescription(),
                foodTruckDocumentUrls
        );
    }
}
