package konkuk.chacall.domain.region.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.chacall.domain.region.application.RegionService;
import konkuk.chacall.domain.region.presentation.dto.request.RegionQueryRequest;
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
}
