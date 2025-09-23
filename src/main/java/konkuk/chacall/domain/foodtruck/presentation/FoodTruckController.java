package konkuk.chacall.domain.foodtruck.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.chacall.domain.foodtruck.application.FoodTruckService;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.FoodTruckSearchRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.FoodTruckResponse;
import konkuk.chacall.global.common.annotation.ExceptionDescription;
import konkuk.chacall.global.common.dto.BaseResponse;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.swagger.SwaggerResponseDescription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FoodTruck API", description = "푸드트럭 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/food-trucks")
@Slf4j
public class FoodTruckController {

    private final FoodTruckService foodTruckService;

    @Operation(
            summary = "푸드트럭 조회",
            description = "필터링 조건을 기반으로 푸드트럭을 조회합니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.DEFAULT)
    @GetMapping
    public BaseResponse<CursorPagingResponse<FoodTruckResponse>> getFoodTrucks(
            @Valid @ParameterObject final FoodTruckSearchRequest request
            ) {
        return BaseResponse.ok(foodTruckService.getFoodTrucks(request));
    }
}
