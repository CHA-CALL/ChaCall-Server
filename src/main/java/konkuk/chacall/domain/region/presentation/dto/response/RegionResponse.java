package konkuk.chacall.domain.region.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegionResponse(
        @Schema(description = "지역 이름", example = "서울")
        String name,

        @Schema(description = "지역 행정동 코드", example = "11")
        Long code
) {
    public static RegionResponse of(String name, Long regionCode) {
        return new RegionResponse(
                name,
                regionCode);
    }
}
