package konkuk.chacall.domain.owner.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateMenuRequest(

        @Schema(description = "메뉴 이름", example = "불고기버거")
        @NotBlank(message = "메뉴 이름은 필수입니다.")
        @Size(max = 18, message = "메뉴 이름은 최대 18자까지 입력 가능합니다.")
        String name,

        @Schema(description = "메뉴 설명", example = "신선한 채소와 불고기를 듬뿍 넣은 수제 버거")
        @NotBlank(message = "메뉴 설명은 필수입니다.")
        @Size(max = 50, message = "메뉴 설명은 최대 50자까지 입력 가능합니다.")
        String description,

        @Schema(description = "메뉴 가격 (원화 단위)", example = "7500")
        @NotNull(message = "가격은 필수입니다.")
        @Positive(message = "가격은 0보다 커야 합니다.")
        Integer price,

        @Schema(description = "대표 메뉴 이미지 URL", example = "https://cdn.example.com/menus/bulgogi-burger.jpg")
        @NotBlank(message = "사진은 반드시 1장 등록해야 합니다.")
        String photoUrl
) {
}
