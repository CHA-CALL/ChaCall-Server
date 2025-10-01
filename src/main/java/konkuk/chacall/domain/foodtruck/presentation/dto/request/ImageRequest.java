package konkuk.chacall.domain.foodtruck.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ImageRequest(
    @Schema(description = "파일 확장자 리스트", example = "[\"png\", \"jpg\"]")
    @NotNull(message = "파일 확장자는 필수입니다.")
    @Size(min = 1, message = "최소 하나 이상의 파일 확장자를 제공해야 합니다.")
    List<String> fileExtensions
) {
}
