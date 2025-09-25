package konkuk.chacall.domain.owner.presentation.dto.request;

import jakarta.validation.constraints.*;

import java.util.List;

public record RegisterMenuRequest(
        @NotBlank(message = "메뉴 이름은 필수입니다.")
        @Size(max = 18, message = "메뉴 이름은 최대 18자까지 입력 가능합니다.")
        String name,

        @NotBlank(message = "메뉴 설명은 필수입니다.")
        @Size(max = 50, message = "메뉴 설명은 최대 50자까지 입력 가능합니다.")
        String description,

        @NotNull(message = "가격은 필수입니다.")
        @Positive(message = "가격은 0보다 커야 합니다.")
        Integer price,

        @NotBlank(message = "사진은 반드시 1장 등록해야 합니다.")
        String photoUrl
) {
}
