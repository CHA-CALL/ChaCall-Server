package konkuk.chacall.domain.region.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.chacall.domain.region.application.RegionService;
import konkuk.chacall.domain.region.presentation.dto.request.RegionQueryRequest;
import konkuk.chacall.domain.region.presentation.dto.request.RegionSearchRequest;
import konkuk.chacall.domain.region.presentation.dto.response.RegionResponse;
import konkuk.chacall.global.common.annotation.ExceptionDescription;
import konkuk.chacall.global.common.dto.BaseResponse;
import konkuk.chacall.global.common.swagger.SwaggerResponseDescription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Region API", description = "지역 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/regions")
@Slf4j
public class RegionController {

    private final RegionService regionService;

    @Operation(
            summary = "지역 조회",
            description = "depth, parentCode 를 기반으로 지역을 조회합니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.GET_REGIONS)
    @GetMapping
    public BaseResponse<List<RegionResponse>> getRegions(
            @Valid @ParameterObject final RegionQueryRequest request
    ) {
        return BaseResponse.ok(regionService.getRegions(request));
    }

    @Operation(
            summary = "지역 검색",
            description = "키워드를 부분문자열로 갖는 모든 지역을 검색합니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.DEFAULT)
    @GetMapping("/search")
    public BaseResponse<List<RegionResponse>> searchRegions(
            @Valid @ParameterObject final RegionSearchRequest request
    ) {
        return BaseResponse.ok(regionService.searchRegions(request));
    }
}
