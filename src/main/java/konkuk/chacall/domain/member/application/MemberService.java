package konkuk.chacall.domain.member.application;

import konkuk.chacall.domain.member.application.foodtruck.SavedFoodTruckService;
import konkuk.chacall.domain.member.application.rating.RatingService;
import konkuk.chacall.domain.member.application.reservation.MemberReservationService;
import konkuk.chacall.domain.member.application.validator.MemberValidator;
import konkuk.chacall.domain.member.presentation.dto.request.*;
import konkuk.chacall.domain.member.presentation.dto.response.*;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final SavedFoodTruckService savedFoodTruckService;
    private final RatingService ratingService;
    private final MemberReservationService memberReservationService;

    private final MemberValidator memberValidator;

    @Transactional
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

    @Transactional
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

    public CursorPagingResponse<MemberReservationHistoryResponse> getMemberReservations(GetReservationHistoryRequest request, Long memberId) {
        // 멤버 유효성 검사 및 조회
        User member = memberValidator.validateAndGetMember(memberId);

        CursorPagingRequest cursorPagingRequest = request.pagingOrDefault();

        return memberReservationService.getMemberReservations(member, request.viewType(), cursorPagingRequest.cursor(), cursorPagingRequest.size());
    }

    public MemberReservationDetailResponse getMemberReservationDetail(Long reservationId, Long memberId) {
        // 멤버 유효성 검사 및 조회
        User member = memberValidator.validateAndGetMember(memberId);

        return memberReservationService.getMemberReservationDetail(reservationId, member);
    }
}
