package konkuk.chacall.domain.foodtruck.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DeleteFoodTruckImagesRequest(
        @Schema(description = "삭제할 이미지 URL 목록", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]")
        @Size(min = 1, message = "최소 1개의 이미지 URL을 제공해야 합니다.")
        List<String> imageUrls
) {
}
