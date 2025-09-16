package konkuk.chacall.domain.member.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.chacall.domain.member.application.MemberService;
import konkuk.chacall.domain.member.presentation.dto.request.RegisterRatingRequest;
import konkuk.chacall.domain.member.presentation.dto.request.UpdateFoodTruckSaveStatusRequest;
import konkuk.chacall.domain.member.presentation.dto.response.RatingResponse;
import konkuk.chacall.domain.member.presentation.dto.response.SavedFoodTruckResponse;
import konkuk.chacall.domain.member.presentation.dto.response.SavedFoodTruckStatusResponse;
import konkuk.chacall.global.common.annotation.ExceptionDescription;
import konkuk.chacall.global.common.annotation.UserId;
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

    @Operation(summary = "푸드트럭 저장상태 변경", description = "푸드트럭 저장상태를 변경합니다. (저장/저장취소)")
    @ExceptionDescription(SwaggerResponseDescription.MEMBER_SAVED_FOOD_TRUCK)
    @PatchMapping("/me/food-trucks/{foodTruckId}")
    public BaseResponse<SavedFoodTruckStatusResponse> updateFoodTruckSaveStatus(
            @Parameter(description = "푸드트럭 식별자", example = "1") final @PathVariable Long foodTruckId,
            @RequestBody @Valid final UpdateFoodTruckSaveStatusRequest request,
            @Parameter(hidden = true) @UserId final Long memberId
    ) {
        return BaseResponse.ok(memberService.updateFoodTruckSaveStatus(request, foodTruckId, memberId));
    }

    @Operation(summary = "저장한 푸드트럭 목록 조회", description = "저장한 푸드트럭 목록을 조회합니다.")
    @GetMapping("/me/food-trucks")
    public BaseResponse<SavedFoodTruckResponse> getSavedFoodTrucks() {
        //todo 커서 페이징 util 완성되면 구현
        return BaseResponse.ok(null);
    }

    @Operation(summary = "평점 등록", description = "지난 예약에 대한 푸드트럭 평점을 남깁니다.")
    @ExceptionDescription(SwaggerResponseDescription.MEMBER_RATING)
    @PostMapping("/me/ratings")
    public BaseResponse<Void> registerRatings(
            @Valid @RequestBody final RegisterRatingRequest request,
            @Parameter(hidden = true) @UserId final Long memberId
    ) {
        memberService.registerRatings(request, memberId);
        return BaseResponse.ok(null);
    }

    @Operation(summary = "평점을 등록할 예약 조회", description = "평점을 등록할 수 있는 지난 예약 목록을 조회합니다.")
    @GetMapping("/me/ratings/reservations")
    public BaseResponse<RatingResponse> getReservationsForRating(
            @Parameter(hidden = true) @UserId final Long memberId
    ) {
        //todo Reservation에서 일정 객체 가져오는 부분 구현되면 완성
        return BaseResponse.ok(null);
    }

}
