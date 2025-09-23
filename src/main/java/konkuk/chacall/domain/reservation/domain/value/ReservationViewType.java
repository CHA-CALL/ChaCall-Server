package konkuk.chacall.domain.reservation.domain.value;

import lombok.Getter;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Getter
public enum ReservationViewType {
    UPCOMING("진행 예정"),
    CONFIRM_REQUEST("확정 신청"),
    COMPLETED("완료 내역"),
    CANCELLED("취소 내역");

    private final String value;

    ReservationViewType(String value) {
        this.value = value;
    }

    // 조회용 상태 매핑
    private static final Map<ReservationViewType, Set<ReservationStatus>> STATUS_GROUPS;

    static {
        Map<ReservationViewType, Set<ReservationStatus>> viewTypeToStatus = new EnumMap<>(ReservationViewType.class);

        // 진행 예정 -> 예약 확정(날짜 안 지난 것), 예약 취소 요청
        viewTypeToStatus.put(UPCOMING, EnumSet.of(
                ReservationStatus.CONFIRMED,
                ReservationStatus.CANCELLED_REQUESTED
        ));

        // 확정 신청 -> 예약 확정 요청
        viewTypeToStatus.put(CONFIRM_REQUEST, EnumSet.of(
                ReservationStatus.CONFIRMED_REQUESTED
        ));

        // 완료 내역 -> 예약 완료(예약 확정 상태에서 날짜 지난 것만)
        viewTypeToStatus.put(COMPLETED, EnumSet.of(
                ReservationStatus.COMPLETED
        ));

        // 취소 내역 -> 예약 취소
        viewTypeToStatus.put(CANCELLED, EnumSet.of(
                ReservationStatus.CANCELLED
        ));

        STATUS_GROUPS = Map.copyOf(viewTypeToStatus);
    }

    public Set<ReservationStatus> getStatuses() {
        return STATUS_GROUPS.getOrDefault(this, Set.of());
    }
}