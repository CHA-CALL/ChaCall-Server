package konkuk.chacall.domain.reservation.presentation.dto.request;

import jakarta.validation.constraints.*;

import java.util.List;

public record CreateReservationRequest(
        @NotNull(message = "푸드트럭 ID는 필수 입력 값입니다.")
        Long foodTruckId,

        @NotBlank(message = "예약 주소는 필수 입력 값입니다.")
        String address,

        @NotBlank(message = "예약 상세 주소는 필수 입력 값입니다.")
        String detailAddress,

        @NotNull(message = "예약 날짜는 필수 입력 값입니다.")
        @Size(min = 1, max = 2, message = "예약 날짜는 최소 1개, 최대 2개까지 작성 가능합니다.")
        List<@Pattern(
                    regexp = "^\\d{4}\\.\\d{2}\\.\\d{2} ~ \\d{4}\\.\\d{2}\\.\\d{2}$",
                    message = "예약 날짜 형식이 올바르지 않습니다. (예: 2025.09.20 ~ 2025.09.20)"
        ) String> reservationDates,

        @NotBlank(message = "운영 시간은 필수 입력 값입니다.")
        @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d) ~ ([01]\\d|2[0-3]):([0-5]\\d)$",
                message = "운영 시간 형식이 올바르지 않습니다. (예: 15:00 ~ 16:00)")
        String operationHour,

        @NotBlank(message = "메뉴는 필수 입력 값입니다.")
        String menu,

        @NotNull(message = "예약금은 필수 입력 값입니다.")
        @PositiveOrZero(message = "예약금은 0 이상이어야 합니다.")
        Integer deposit,

        @NotNull(message = "전기 사용 여부는 필수 입력 값입니다.")
        Boolean isUseElectricity,

        String etcRequest
) {
}
