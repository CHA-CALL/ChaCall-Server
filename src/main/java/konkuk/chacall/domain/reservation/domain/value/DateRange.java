package konkuk.chacall.domain.reservation.domain.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;

import java.time.LocalDate;

@Embeddable
public record DateRange(
        @Column(name = "start_date", nullable = false)
        LocalDate startDate,

        @Column(name = "end_date", nullable = false)
        LocalDate endDate
) {
    /**
     * 객체 생성 시 유효성 검증 (종료일이 시작일보다 빠를 수 없음)
     */
    public DateRange {
        if (endDate.isBefore(startDate)) {
            throw new DomainRuleException(ErrorCode.INVALID_DATE_RANGE);
        }
    }
}
