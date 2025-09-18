package konkuk.chacall.domain.member.application.reservation;

import konkuk.chacall.domain.member.presentation.dto.request.GetReservationHistoryRequest;
import konkuk.chacall.domain.member.presentation.dto.response.MemberReservationDetailResponse;
import konkuk.chacall.domain.member.presentation.dto.response.MemberReservationHistoryResponse;
import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.repository.ReservationRepository;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberReservationService {

    private final ReservationRepository reservationRepository;

    public CursorPagingResponse<MemberReservationHistoryResponse> getMemberReservations(User member, GetReservationHistoryRequest request, Long lastCursor, int pageSize) {
        Slice<Reservation> memberReservationSlice = reservationRepository
                .findMemberReservationsByStatusWithCursor(member.getUserId(), request.status(), lastCursor, PageRequest.of(0, pageSize));
        List<Reservation> memberReservationList = memberReservationSlice.getContent();

        List<MemberReservationHistoryResponse> responses = memberReservationList.stream()
                .map(reservation -> MemberReservationHistoryResponse.of(reservation, reservation.getFoodTruck()))
                .toList();

        return CursorPagingResponse.of(responses, MemberReservationHistoryResponse::reservationId, memberReservationSlice.hasNext());
    }

    public MemberReservationDetailResponse getMemberReservationDetail(Long reservationId, User member) {
        Reservation reservation = reservationRepository.findByIdWithDetails(reservationId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.RESERVATION_NOT_FOUND));

        if(!reservation.isReservedBy(member.getUserId())) {
            throw new BusinessException(ErrorCode.RESERVATION_NOT_OWNED);
        }

        return MemberReservationDetailResponse.of(reservation, reservation.getFoodTruck());
    }
}
