package konkuk.chacall.domain.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.chacall.domain.user.presentation.dto.request.ApproveFoodTruckStatusRequest;
import konkuk.chacall.domain.user.application.UserService;
import konkuk.chacall.domain.user.presentation.dto.request.UpdateUserInfoRequest;
import konkuk.chacall.domain.user.presentation.dto.response.UserResponse;
import konkuk.chacall.global.common.annotation.ExceptionDescription;
import konkuk.chacall.global.common.annotation.UserId;
import konkuk.chacall.global.common.dto.BaseResponse;
import konkuk.chacall.global.common.swagger.SwaggerResponseDescription;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API", description = "전체 사용자(마이페이지) 관련 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "[마이페이지] 회원 정보 조회",
            description = "사용자(고객)의 정보를 조회합니다. (사장님, 일반유저 무관)"
    )
    @ExceptionDescription(SwaggerResponseDescription.GET_USER_INFO)
    @GetMapping("/me")
    public BaseResponse<UserResponse> getUserInfo(
            @Parameter(hidden = true) @UserId final Long userId
    ) {
        return BaseResponse.ok(userService.getUserInfo(userId));
    }

    @Operation(
            summary = "[마이페이지] 회원 정보 수정",
            description = "사용자(고객)의 정보를 수정합니다. (사장님, 일반유저 무관)"
    )
    @ExceptionDescription(SwaggerResponseDescription.UPDATE_USER_INFO)
    @PutMapping("/me")
    public BaseResponse<Void> updateUserInfo(
            @RequestBody @Valid final UpdateUserInfoRequest request,
            @Parameter(hidden = true) @UserId final Long userId
    ) {
         userService.updateUserInfo(userId, request);

         return BaseResponse.ok(null);
    }

    @Operation(
            summary = "푸드트럭 승인 상태 변경",
            description = "운영자 - 푸드트럭 승인 상태를 변경합니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.APPROVE_FOOD_TRUCK_STATUS)
    @PatchMapping("/admin/food-trucks/{foodTruckId}/approval")
    public BaseResponse<Void> approveFoodTruckStatus (
            @PathVariable final Long foodTruckId,
            @Valid @RequestBody final ApproveFoodTruckStatusRequest request,
            @Parameter(hidden = true) @UserId final Long userId
    ) {
        userService.approveFoodTruckStatus(userId, foodTruckId, request);
        return BaseResponse.ok(null);
    }
}
