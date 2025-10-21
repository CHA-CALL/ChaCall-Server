package konkuk.chacall.domain.owner.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record FoodTruckCreateRequest(
        @Schema(description = "푸드트럭 이름", example = "차콜 푸드트럭")
        @NotBlank(message = "푸드트럭 이름은 필수입니다.")
        @Size(min = 1, max = 10, message = "푸드트럭 이름은 1~10자입니다.")
        String name,

        // 사업자등록증 1장
        @Schema(description = "사업자등록증 url", example = "https://cdn.chacall.com/foodtrucks/osori/business-license.jpg")
        @NotBlank(message = "사업자등록증 url 은 필수입니다.")
        String businessRegistrationUrl,

        // 기타 서류 5장
        @Schema(description = "기타 서류 URL 목록 (정확히 5장)", example = "[\"https://cdn.chacall.com/foodtrucks/osori/doc1.jpg\", \"https://cdn.chacall.com/foodtrucks/osori/doc2.jpg\", \"https://cdn.chacall.com/foodtrucks/osori/doc3.jpg\", \"https://cdn.chacall.com/foodtrucks/osori/doc4.jpg\", \"https://cdn.chacall.com/foodtrucks/osori/doc5.jpg\"]")
        @Size(min = 5, max = 5, message = "기타 서류는 정확히 5장을 업로드해야합니다.")
        List<@NotBlank String> otherDocumentUrls
) {
}
