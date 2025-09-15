package konkuk.chacall.domain.member.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.chacall.domain.member.application.MemberService;
import konkuk.chacall.domain.member.presentation.dto.request.UpdateFoodTruckSaveStatusRequest;
import konkuk.chacall.domain.member.presentation.dto.response.SavedFoodTruckResponse;
import konkuk.chacall.global.common.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member API", description = "일반 유저 관련 API")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "푸드트럭 저장상태 변경", description = "푸드트럭 저장상태를 변경합니다. (저장/저장취소)" )
    @PatchMapping("/me/food-trucks/{foodTruckId}")
    public BaseResponse<SavedFoodTruckResponse> updateFoodTruckSaveStatus(
            @Parameter(description = "푸드트럭 식별자", example = "1") final @PathVariable Long foodTruckId,
            final @RequestBody @Valid UpdateFoodTruckSaveStatusRequest request
    ) {
        return BaseResponse.ok(memberService.updateFoodTruckSaveStatus(request, foodTruckId, 1L));
    }
}
