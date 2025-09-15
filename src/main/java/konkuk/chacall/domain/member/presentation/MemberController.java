package konkuk.chacall.domain.member.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.chacall.domain.member.application.MemberService;
import konkuk.chacall.domain.member.presentation.dto.request.UpdateFoodTruckSaveStatusRequest;
import konkuk.chacall.domain.member.presentation.dto.response.SavedFoodTruckResponse;
import konkuk.chacall.domain.member.presentation.dto.response.SavedFoodTruckStatusResponse;
import konkuk.chacall.global.common.annotation.ExceptionDescription;
import konkuk.chacall.global.common.dto.BaseResponse;
import konkuk.chacall.global.common.swagger.SwaggerResponseDescription;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member API", description = "일반 유저 관련 API")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "푸드트럭 저장상태 변경", description = "푸드트럭 저장상태를 변경합니다. (저장/저장취소)" )
    @ExceptionDescription(SwaggerResponseDescription.MEMBER_SAVED_FOOD_TRUCK)
    @PatchMapping("/me/food-trucks/{foodTruckId}")
    public BaseResponse<SavedFoodTruckStatusResponse> updateFoodTruckSaveStatus(
            @Parameter(description = "푸드트럭 식별자", example = "1") final @PathVariable Long foodTruckId,
            final @RequestBody @Valid UpdateFoodTruckSaveStatusRequest request
    ) {
        //todo 인증 구현되면 userId 동적 처리
        return BaseResponse.ok(memberService.updateFoodTruckSaveStatus(request, foodTruckId, 1L));
    }

    @Operation(summary = "저장한 푸드트럭 목록 조회", description = "저장한 푸드트럭 목록을 조회합니다." )
    @GetMapping("/me/food-trucks")
    public BaseResponse<SavedFoodTruckResponse> getSavedFoodTrucks() {
        //todo 커서 페이징 util 완성되면 구현
        return BaseResponse.ok(null);
    }

    @Operation(summary = "평점 남기기", description = "지난 예약에 대한 푸드트럭 평점을 남깁니다." )
    @PostMapping("/me/ratings")
    public BaseResponse<Void> registerRatings() {
        //todo 구현 예정
        return BaseResponse.ok(null);
    }

}
