package konkuk.chacall.domain.owner.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyFoodTrucksResponse(
        @Schema(description = "푸드트럭 식별자", example = "1")
        Long foodTruckId,
        @Schema(description = "푸드트럭 이미지", example = "image.png")
        String imageUrl,
        @Schema(description = "푸드트럭 이름", example = "차콜 푸드트럭")
        String name,
        @Schema(description = "푸드트럭 설명", example = "저희 푸드트럭은 10년간 이어져온...")
        String description,
        @Schema(description = "운영 가능 시간대", example = "09:00 ~ 21:00")
        String activeTime,
        @Schema(description = "호출 가능 지역", example = "서울 전체, 경기도 수원시 영통구, 인천 계양구")
        String serviceArea
) {
}
