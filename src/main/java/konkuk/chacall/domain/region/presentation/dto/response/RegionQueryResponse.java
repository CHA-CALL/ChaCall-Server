package konkuk.chacall.domain.region.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.region.domain.model.Region;

public record RegionQueryResponse(
        @Schema(description = "지역 이름", example = "서울")
        String name,

        @Schema(description = "지역 행정동 코드", example = "11")
        Long code
) {
        public static RegionQueryResponse of(Region region) {
                return new RegionQueryResponse(
                        region.getName(),
                        region.getRegionCode());
        }
}
