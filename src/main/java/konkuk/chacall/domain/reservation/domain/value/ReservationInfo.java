package konkuk.chacall.domain.reservation.domain.value;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import konkuk.chacall.global.common.converter.ReservationDateListConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Embeddable
public class ReservationInfo {

    @Column(nullable = false)
    private String reservationAddress; // 주소 (시/동/구)

    @Column(nullable = false)
    private String reservationDetailAddress; // 상세 주소

    @Convert(converter = ReservationDateListConverter.class)
    @Column(nullable = false)
    private ReservationDateList reservationDate; // 예약 일정

    @Column(nullable = false, length = 50)
    private String operationHour; // 운영 시간대

    @Column(name = "menu", nullable = false)
    private String menu;

    @Column(nullable = false)
    private Integer reservationDeposit; // 예약금

    @Column(name = "is_use_electricity", nullable = false)
    private boolean isUseElectricity; // 전기 사용 여부

    @Column(length = 1000)
    private String etcRequest; // 기타 요청 사항

    public List<String> getFormattedDateTimeInfos() {
        DateTimeFormatter DOT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return this.reservationDate.getRanges().stream()
                .map(date -> date.startDate().format(DOT) + " ~ " + date.endDate().format(DOT)
                        + " " + this.operationHour)
                .toList();
    }

    public String parsingIsUserElectricity() {
        return isUseElectricity ? "가능" : "불가능";
    }

    public String parsingReservationDeposit() {
        return reservationDeposit + "원";
    }
}