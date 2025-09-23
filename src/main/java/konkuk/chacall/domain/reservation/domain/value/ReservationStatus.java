package konkuk.chacall.domain.reservation.domain.value;

import com.fasterxml.jackson.annotation.JsonValue;
import konkuk.chacall.global.common.dto.EnumValue;
import lombok.Getter;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
public enum ReservationStatus implements EnumValue {
    PENDING("예약 대기"),

    CONFIRMED("예약 확정"),
    CONFIRMED_REQUESTED("예약 확정 요청"),

    CANCELLED("예약 취소"),
    CANCELLED_REQUESTED("예약 취소 요청"),

    COMPLETED("예약 완료")
    ;

    @JsonValue
    private final String value;

    ReservationStatus(String value) {
        this.value = value;
    }

    // 상태별 허용되는 다음 상태들을 Map으로 관리
    // PENDING -> CONFIRMED_REQUESTED -> CONFIRMED -> CANCELLED_REQUESTED -> CANCELLED
    private static final Map<ReservationStatus, Set<ReservationStatus>> TRANSITIONS;
    static {
        Map<ReservationStatus, Set<ReservationStatus>> m = new EnumMap<>(ReservationStatus.class);
        m.put(PENDING, EnumSet.of(CONFIRMED_REQUESTED));
        m.put(CONFIRMED_REQUESTED, EnumSet.of(CONFIRMED));
        m.put(CONFIRMED, EnumSet.of(CANCELLED_REQUESTED));
        m.put(CANCELLED_REQUESTED, EnumSet.of(CANCELLED));
        m.put(CANCELLED, EnumSet.noneOf(ReservationStatus.class));
        TRANSITIONS = Map.copyOf(m);
    }

    public boolean canTransitTo(ReservationStatus target) {
        Objects.requireNonNull(target, "바꾸려는 예약 상태는 null일 수 없습니다.");
        return TRANSITIONS.getOrDefault(this, Set.of()).contains(target);
    }

    public boolean isInValidStatusTransitionFrom(ReservationStatus newStatus) {
        return !canTransitTo(newStatus);
    }
}