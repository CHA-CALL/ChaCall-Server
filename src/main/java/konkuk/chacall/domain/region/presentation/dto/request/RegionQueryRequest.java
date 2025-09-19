package konkuk.chacall.domain.region.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import konkuk.chacall.domain.region.presentation.dto.validator.ValidRegionQuery;

@ValidRegionQuery
public record RegionQueryRequest(
        @Schema(description = "행정 구역 깊이 (1: 시/도, 2: 시/군/구, 3: 동/읍/면)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "depth 는 필수 값입니다.")
        @Min(value = 1, message = "depth 는 1 이상이어야 합니다.")
        @Max(value = 3, message = "depth 는 3 이하여야 합니다.")
        Integer depth,
        @Schema(description = "부모 행정동 코드 (depth=2/3일 때 필수)", example = "11", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Long parentCode
) {
}
