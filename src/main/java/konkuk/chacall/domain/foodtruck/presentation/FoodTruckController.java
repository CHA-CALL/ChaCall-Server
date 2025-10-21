package konkuk.chacall.domain.foodtruck.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.chacall.domain.foodtruck.application.FoodTruckService;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.*;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.*;
import konkuk.chacall.global.common.annotation.ExceptionDescription;
import konkuk.chacall.global.common.annotation.UserId;
import konkuk.chacall.global.common.dto.BaseResponse;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.swagger.SwaggerResponseDescription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

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
            @Valid @ParameterObject final FoodTruckSearchRequest request,
            @Parameter(hidden = true) @UserId final Long memberId

    ) {
        return BaseResponse.ok(foodTruckService.getFoodTrucks(memberId, request));
    }

    @Operation(
            summary = "푸드트럭 이름 중복 체크",
            description = "푸드트럭 이름 중복 여부를 체크합니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.DEFAULT)
    @PostMapping("/duplicate-check")
    public BaseResponse<FoodTruckNameDuplicateCheckResponse> isNameDuplicated(
            @Valid @RequestBody final FoodTruckNameDuplicateCheckRequest request,
            @Parameter(hidden = true) @UserId final Long ownerId
    ) {
        return BaseResponse.ok(foodTruckService.isNameDuplicated(ownerId, request));
    }

    @Operation(
            summary = "푸드트럭 이미지 presigned URL 발급",
            description = "푸드트럭 사진을 업로드하기 위한 presigned URL을 발급받습니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.GET_FOOD_TRUCK_PRESIGEND_URL)
    @PostMapping("/images")
    public BaseResponse<ImageResponse> createFoodTruckImagePresignedUrl(
            @Valid @RequestBody final ImageRequest request,
            @Parameter(hidden = true) @UserId final Long ownerId
    ) {
        return BaseResponse.ok(foodTruckService.createFoodTruckImagePresignedUrl(request, ownerId));
    }

    @Operation(
            summary = "메뉴 이미지 presigned URL 발급",
            description = "메뉴 사진을 업로드하기 위한 presigned URL을 발급받습니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.GET_MENU_PRESIGEND_URL)
    @PostMapping("/menus/images")
    public BaseResponse<ImageResponse> createMenuImagePresignedUrl(
            @Valid @RequestBody final ImageRequest request,
            @Parameter(hidden = true) @UserId final Long ownerId
    ) {
        return BaseResponse.ok(foodTruckService.createMenuImagePresignedUrl(request, ownerId));
    }


    @Operation(
            summary = "푸드트럭 메뉴 목록 조회",
            description = "푸드트럭 메뉴 목록을 조회합니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.GET_FOOD_TRUCK_MENUS)
    @GetMapping("/{foodTruckId}/menus")
    public BaseResponse<CursorPagingResponse<FoodTruckMenuResponse>> getFoodTruckMenus (
            @PathVariable final Long foodTruckId,
            @ParameterObject final FoodTruckMenuRequest request,
            @Parameter(hidden = true) @UserId final Long memberId) {
        return BaseResponse.ok(foodTruckService.getFoodTruckMenus(memberId, foodTruckId, request));
    }

    @Operation(
            summary = "나의 푸드트럭 정보 기입/수정",
            description = "승인이 완료된 나의 푸드트럭 정보를 기입하거나 수정합니다."
    )
    @PutMapping("{foodTruckId}")
    public BaseResponse<FoodTruckIdResponse> updateMyFoodTruckInfo(
            @PathVariable final Long foodTruckId,
            @Valid @RequestBody final UpdateFoodTruckInfoRequest request,
            @Parameter(hidden = true) @UserId final Long ownerId
    ) {
        return BaseResponse.ok(foodTruckService.updateMyFoodTruckInfo(ownerId, foodTruckId, request));
    }


}
