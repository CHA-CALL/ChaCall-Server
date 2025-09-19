package konkuk.chacall.domain.reservation.domain.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
    PENDING("예약 대기"),

    CONFIRMED("예약 확정 완료"),
    CONFIRMED_REQUESTED("예약 확정 요청"),

    CANCELLED("예약 취소 완료"),
    CANCELED_REQUESTED("예약 취소 요청")

    ;

    private final String value;
}
