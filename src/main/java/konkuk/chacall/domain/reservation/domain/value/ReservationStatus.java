package konkuk.chacall.domain.reservation.domain.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
    PENDING("예약 대기"),
    CONFIRMED("예약 확정"),
    CANCELLED("예약 취소");

    private final String value;
}
