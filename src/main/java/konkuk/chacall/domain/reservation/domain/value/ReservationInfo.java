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
    private String address; // 주소 (시/동/구)

    @Column(nullable = false)
    private String detailAddress; // 상세 주소

    @Convert(converter = ReservationDateListConverter.class)
    @Column(nullable = false)
    private ReservationDateList reservationDates; // 예약 일정

    @Column(nullable = false, length = 50)
    private String operationHour; // 운영 시간대

    @Column(name = "menu", nullable = false)
    private String menu;

    @Column(nullable = false)
    private Integer deposit; // 예약금

    @Column(name = "is_use_electricity", nullable = false)
    private boolean isUseElectricity; // 전기 사용 여부

    @Column(length = 1000)
    private String etcRequest; // 기타 요청 사항

    /**
     * 예약 날짜와 운영 시간을 "YYYY.MM.DD ~ YYYY.MM.DD HH:MM ~ HH:MM" 형식으로 반환
     */
    public List<String> getFormattedDateTimeInfos() {
        DateTimeFormatter DOT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return this.reservationDates.getRanges().stream()
                .map(date -> date.startDate().format(DOT) + " ~ " + date.endDate().format(DOT)
                        + " " + this.operationHour)
                .toList();
    }

    /**
     * 예약 날짜를 "YYYY.MM.DD ~ YYYY.MM.DD" 형식으로 반환
     */
    public List<String> getFormattedDateInfos() {
        DateTimeFormatter DOT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return this.reservationDates.getRanges().stream()
                .map(date -> date.startDate().format(DOT) + " ~ " + date.endDate().format(DOT))
                .toList();
    }

    public String getFullAddress() {
        return this.address + " " + this.detailAddress;
    }

    public String parsingIsUserElectricity() {
        return isUseElectricity ? "가능" : "불가능";
    }

    public String parsingReservationDeposit() {
        return deposit + "원";
    }

    public void updateReservationInfo(
            String reservationAddress,
            String reservationDetailAddress,
            ReservationDateList reservationDate,
            String operationHour,
            String menu,
            Integer reservationDeposit,
            boolean isUseElectricity,
            String etcRequest
    ) {
        this.address = reservationAddress;
        this.detailAddress = reservationDetailAddress;
        this.reservationDates = reservationDate;
        this.operationHour = operationHour;
        this.menu = menu;
        this.deposit = reservationDeposit;
        this.isUseElectricity = isUseElectricity;
        this.etcRequest = etcRequest;
    }
}