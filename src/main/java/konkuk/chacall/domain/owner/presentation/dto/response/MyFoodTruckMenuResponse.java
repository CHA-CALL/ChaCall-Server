package konkuk.chacall.domain.owner.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.foodtruck.domain.model.Menu;

@Schema(description = "메뉴 응답")
public record MyFoodTruckMenuResponse(
        @Schema(description = "메뉴 ID", example = "101")
        Long menuId,
        @Schema(description = "메뉴명", example = "크림파스타")
        String name,
        @Schema(description = "가격", example = "12000원")
        String price,
        @Schema(description = "설명", example = "진한 크림소스와 베이컨")
        String description,
        @Schema(description = "이미지 URL", example = "https://cdn.example.com/menus/101.jpg")
        String imageUrl,
        @Schema(description = "노출 상태 코드", example = "ON/OFF")
        String status
) {
    public static MyFoodTruckMenuResponse from(Menu menu) {

        return new MyFoodTruckMenuResponse(
                menu.getMenuId(),
                menu.getName(),
                menu.parsingMenuPrice(),
                menu.getDescription(),
                menu.getImageUrl(),
                menu.getMenuStatus().name()
        );
    }
}
