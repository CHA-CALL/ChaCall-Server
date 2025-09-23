package konkuk.chacall.domain.reservation.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;

public record UpdateReservationRequest(
        @Schema(description = "예약 주소 (시/구/동)", example = "서울시 강남구 역삼동")
        @NotBlank(message = "예약 주소는 필수 입력 값입니다.")
        @Size(max = 20, message = "예약 주소는 공백 포함 최대 20자까지 입력 가능합니다.")
        String address,

        @Schema(description = "예약 상세 주소", example = "역삼로 123")
        @NotBlank(message = "예약 상세 주소는 필수 입력 값입니다.")
        @Size(max = 20, message = "예약 상세 주소는 공백 포함 최대 20자까지 입력 가능합니다.")
        String detailAddress,

        @Schema(description = "예약 날짜 (최소 1개, 최대 2개) (형식: YYYY.MM.DD ~ YYYY.MM.DD)", example = "[\"2025.09.20 ~ 2025.09.20\", \"2025.09.25 ~ 2025.09.25\"]")
        @NotNull(message = "예약 날짜는 필수 입력 값입니다.")
        @Size(min = 1, max = 2, message = "예약 날짜는 최소 1개, 최대 2개까지 작성 가능합니다.")
        List<@Pattern(
                regexp = "^\\d{4}\\.\\d{2}\\.\\d{2} ~ \\d{4}\\.\\d{2}\\.\\d{2}$",
                message = "예약 날짜 형식이 올바르지 않습니다. (예: 2025.09.20 ~ 2025.09.20)"
        ) String> reservationDates,

        @Schema(description = "운영 시간 (형식: HH:MM ~ HH:MM)", example = "15:00 ~ 16:00")
        @NotBlank(message = "운영 시간은 필수 입력 값입니다.")
        @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d) ~ ([01]\\d|2[0-3]):([0-5]\\d)$",
                message = "운영 시간 형식이 올바르지 않습니다. (예: 15:00 ~ 16:00)")
        String operationHour,

        @Schema(description = "메뉴", example = "떡볶이, 순대, 튀김")
        @NotBlank(message = "메뉴는 필수 입력 값입니다.")
        @Size(max = 50, message = "메뉴는 공백 포함 최대 50자까지 입력 가능합니다.")
        String menu,

        @Schema(description = "예약금 (0 이상)", example = "50000")
        @NotNull(message = "예약금은 필수 입력 값입니다.")
        @PositiveOrZero(message = "예약금은 0 이상이어야 합니다.")
        @Digits(integer = 10, fraction = 0, message = "예약금은 공백 포함 최대 10자리까지 입력 가능합니다.")
        Integer deposit,

        @Schema(description = "전기 사용 여부", example = "true")
        @NotNull(message = "전기 사용 여부는 필수 입력 값입니다.")
        Boolean isUseElectricity,

        @Schema(description = "기타 요청 사항", example = "주차 공간이 넓었으면 좋겠습니다.")
        @Size(max = 200, message = "기타 요청 사항은 공백 포함 최대 200자까지 입력 가능합니다.")
        String etcRequest
) {
}
