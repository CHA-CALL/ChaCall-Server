package konkuk.chacall.domain.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.chacall.domain.user.application.UserService;
import konkuk.chacall.domain.user.presentation.dto.request.UpdateUserInfoRequest;
import konkuk.chacall.domain.user.presentation.dto.response.GetUserInfoResponse;
import konkuk.chacall.global.common.annotation.ExceptionDescription;
import konkuk.chacall.global.common.dto.BaseResponse;
import konkuk.chacall.global.common.swagger.SwaggerResponseDescription;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "전체 사용자(마이페이지) 관련 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "[마이페이지] 회원 정보 조회", description = "사용자(고객)의 정보를 조회합니다. (사장님, 일반유저 무관)")
    @ExceptionDescription(SwaggerResponseDescription.GET_USER_INFO)
    @GetMapping("/me")
    public BaseResponse<GetUserInfoResponse> getUserInfo() {
        // todo 추후에 토큰 추가될 시 id 값은 토큰에서 추출하여 전달
        return BaseResponse.ok(userService.getUserInfo(1L));
    }

    @Operation(summary = "[마이페이지] 회원 정보 수정", description = "사용자(고객)의 정보를 수정합니다. (사장님, 일반유저 무관)")
    @ExceptionDescription(SwaggerResponseDescription.UPDATE_USER_INFO)
    @PatchMapping("/me")
    public BaseResponse<Void> updateUserInfo(UpdateUserInfoRequest request) {
        // todo 추후에 토큰 추가될 시 id 값은 토큰에서 추출하여 전달
         userService.updateUserInfo(1L, request);

         return BaseResponse.ok(null);
    }
}
