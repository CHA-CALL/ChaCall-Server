package konkuk.chacall.domain.member.application;

import konkuk.chacall.domain.member.application.foodtruck.SavedFoodTruckService;
import konkuk.chacall.domain.member.application.rating.RatingService;
import konkuk.chacall.domain.member.application.validator.MemberValidator;
import konkuk.chacall.domain.member.presentation.dto.request.RegisterRatingRequest;
import konkuk.chacall.domain.member.presentation.dto.request.UpdateFoodTruckSaveStatusRequest;
import konkuk.chacall.domain.member.presentation.dto.response.ReservationForRatingResponse;
import konkuk.chacall.domain.member.presentation.dto.response.SavedFoodTruckResponse;
import konkuk.chacall.domain.member.presentation.dto.response.SavedFoodTruckStatusResponse;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final SavedFoodTruckService savedFoodTruckService;
    private final RatingService ratingService;

    private final MemberValidator memberValidator;

    public SavedFoodTruckStatusResponse updateFoodTruckSaveStatus(UpdateFoodTruckSaveStatusRequest request, Long foodTruckId, Long memberId) {
        // 멤버 유효성 검사 및 조회
        User member = memberValidator.validateAndGetMember(memberId);

        // 저장 또는 저장 취소 처리
        return savedFoodTruckService.updateFoodTruckSaveStatus(request, foodTruckId, member);
    }

    public CursorPagingResponse<SavedFoodTruckResponse> getSavedFoodTrucks(CursorPagingRequest cursorPagingRequest, Long memberId) {
        // 멤버 유효성 검사 및 조회
        User member = memberValidator.validateAndGetMember(memberId);

        return savedFoodTruckService.getSavedFoodTrucks(cursorPagingRequest, member);
    }

    public void registerRatings(RegisterRatingRequest request, Long memberId) {
        // 멤버 유효성 검사 및 조회
        User member = memberValidator.validateAndGetMember(memberId);

        // 평점 등록 처리
        ratingService.registerRatings(request, member);
    }


    public ReservationForRatingResponse getReservationsForRating(Long memberId) {
        // 멤버 유효성 검사 및 조회
        User member = memberValidator.validateAndGetMember(memberId);

        return ratingService.getReservationsForRating(member);
    }
}
