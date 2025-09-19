package konkuk.chacall.domain.reservation.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.value.ReservationInfo;

import java.util.List;

public record ReservationResponse(
        @Schema(description = "예약 주소 (시/구/동)", example = "서울시 강남구 역삼동")
        String address,

        @Schema(description = "예약 상세 주소", example = "역삼로 123")
        String detailAddress,

        @Schema(description = "예약 날짜 (형식: YYYY.MM.DD ~ YYYY.MM.DD)", example = "[\"2025.09.20 ~ 2025.09.20\", \"2025.09.25 ~ 2025.09.25\"]")
        List<@Pattern(
                regexp = "^\\d{4}\\.\\d{2}\\.\\d{2} ~ \\d{4}\\.\\d{2}\\.\\d{2}$",
                message = "예약 날짜 형식이 올바르지 않습니다. (예: 2025.09.20 ~ 2025.09.20)"
        ) String> reservationDates,

        @Schema(description = "운영 시간 (형식: HH:MM ~ HH:MM)", example = "15:00 ~ 16:00")
        String operationHour,

        @Schema(description = "메뉴", example = "떡볶이, 순대, 튀김")
        String menu,

        @Schema(description = "예약금 (0 이상)", example = "50000")
        Integer deposit,

        @Schema(description = "전기 사용 여부", example = "true")
        Boolean isUseElectricity,

        @Schema(description = "기타 요청 사항", example = "주차 공간이 넓었으면 좋겠습니다.")
        String etcRequest
) {
        public static ReservationResponse of(Reservation reservation) {
                ReservationInfo reservationInfo = reservation.getReservationInfo();
                return new ReservationResponse(
                        reservationInfo.getReservationAddress(),
                        reservationInfo.getReservationDetailAddress(),
                        reservationInfo.getFormattedDateInfos(),
                        reservationInfo.getOperationHour(),
                        reservationInfo.getMenu(),
                        reservationInfo.getReservationDeposit(),
                        reservationInfo.isUseElectricity(),
                        reservationInfo.getEtcRequest()
            );
        }
}
