package konkuk.chacall.domain.region.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RegionSearchRequest(
        @Schema(description = "지역명 검색 키워드(부분일치, fullName 기준)", example = "광", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "keyword 는 필수입니다.")
        String keyword
) { }
