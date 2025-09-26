package konkuk.chacall.domain.owner.application.reservation;

import konkuk.chacall.domain.owner.presentation.dto.response.OwnerReservationDetailResponse;
import konkuk.chacall.domain.owner.presentation.dto.response.OwnerReservationHistoryResponse;
import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.repository.ReservationRepository;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;
import konkuk.chacall.domain.reservation.domain.value.ReservationViewType;
import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.domain.user.domain.repository.UserRepository;
import konkuk.chacall.global.common.domain.BaseStatus;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OwnerReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public CursorPagingResponse<OwnerReservationHistoryResponse> getOwnerReservations(Long ownerId, ReservationViewType viewType, Long lastCursor, int pageSize) {
        // 예약 목록 조회
        Slice<Reservation> ownerReservationSlice = findReservations(ownerId, viewType.getStatuses(), lastCursor, pageSize);
        List<Reservation> ownerReservationList = ownerReservationSlice.getContent();

        // 손님 ID 리스트로 User 정보 한 번에 조회
        Map<Long, User> customerMap = getCustomerMap(ownerReservationList);

        // 미리 조회한 User 정보를 사용해 DTO 로 변환
        List<OwnerReservationHistoryResponse> responses = mapToReservationHistory(ownerReservationList, customerMap);

        return CursorPagingResponse.of(responses, OwnerReservationHistoryResponse::reservationId, ownerReservationSlice.hasNext());
    }

    public OwnerReservationDetailResponse getReservationDetail(Long ownerId, Long reservationId) {
        // ID로 예약 정보와 연관된 모든 데이터 한 번에 조회
        Reservation reservation = reservationRepository.findByIdWithDetails(reservationId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.RESERVATION_NOT_FOUND));

        reservation.validateFoodTruckOwner(ownerId);

        // DTO 로 변환하여 반환
        return OwnerReservationDetailResponse.of(reservation, reservation.getMember());
    }

    /**
     * 예약 목록 조회
     */
    private Slice<Reservation> findReservations(Long ownerId, Set<ReservationStatus> statuses, Long lastCursor, int pageSize) {
        return reservationRepository
                .findOwnerReservationsByStatusWithCursor(ownerId, statuses, lastCursor, PageRequest.of(0, pageSize));
    }

    /**
     * 예약 목록에 포함된 손님 정보 Map 조회
     */
    private Map<Long, User> getCustomerMap(List<Reservation> reservations) {
        List<Long> customerIds = reservations.stream()
                .map(reservation -> reservation.getMember().getUserId())
                .toList();

        return userRepository.findAllByUserIdInAndRoleAndStatus(customerIds, Role.MEMBER, BaseStatus.ACTIVE).stream()
                .collect(Collectors.toMap(User::getUserId, user -> user));
    }

    /**
     * 예약 목록과 손님 정보를 조합하여 DTO 리스트로 변환
     */
    private List<OwnerReservationHistoryResponse> mapToReservationHistory(List<Reservation> reservations, Map<Long, User> customerMap) {
        return reservations.stream()
                .map(reservation -> {
                    User customer = customerMap.get(reservation.getMember().getUserId());
                    return OwnerReservationHistoryResponse.of(reservation, customer);
                })
                .toList();
    }
}
