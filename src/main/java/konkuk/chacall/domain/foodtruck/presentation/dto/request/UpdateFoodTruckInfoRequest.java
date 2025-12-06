package konkuk.chacall.domain.foodtruck.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import konkuk.chacall.domain.foodtruck.domain.value.AvailableQuantity;
import konkuk.chacall.domain.foodtruck.domain.value.MenuCategory;
import konkuk.chacall.domain.foodtruck.domain.value.NeedElectricity;
import konkuk.chacall.domain.foodtruck.domain.value.PaymentMethod;

import java.util.List;
import java.util.Set;

public record UpdateFoodTruckInfoRequest (
        @Schema(description = "푸드트럭 이름", example = "맛있는 푸드트럭")
        @NotBlank(message = "푸드트럭 이름은 필수 입력 값입니다.")
        String name,

        @Schema(description = "푸드트럭 설명", example = "저희 푸드트럭은 신선한 재료로 만든 음식을 제공합니다.")
        @NotBlank(message = "푸드트럭 설명은 필수 입력 값입니다.")
        String description,

        @Schema(description = "푸드트럭 전화번호", example = "010-1234-5678")
        @NotBlank(message = "푸드트럭 전화번호는 필수 입력 값입니다.")
        String phoneNumber,

        @Schema(description = "운영 시간대 (형식: HH:MM-HH:MM)", example = "10:00-18:00")
        @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d-(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "운영 시간대는 'HH:MM-HH:MM' 형식이어야 합니다.")
        @NotBlank(message = "운영 시간대는 필수 입력 값입니다.")
        String activeTime,

        @Schema(description = "시간 상의 필요 여부", example = "true")
        @NotNull(message = "시간 상의 필요 여부는 필수 입력 값입니다.")
        Boolean timeDiscussRequired,

        @Schema(description = "푸드트럭 서비스 가능 지역 ID 리스트", example = "[1, 2, 3]")
        @Size(min = 1, max = 10, message = "서비스 가능 지역은 최소 1개 최대 10개 선택 가능합니다.")
        Set<@NotNull Long> foodTruckServiceAreas,

        @Schema(description = "메뉴 카테고리 리스트", example = "[\"한식\", \"분식\"]")
        @Size(min = 1, max = 12, message = "메뉴 카테고리는 최소 1개 최대 12개 선택 가능합니다.")
        List<@NotNull MenuCategory> menuCategories,

        @Schema(description = "제조 가능 수량", example = "100인분 미만", allowableValues = {
                "50인분 미만",
                "100인분 미만",
                "150인분 미만",
                "200인분 미만",
                "200인분 이상",
                "논의 필요"
        })
        @NotNull(message = "제조 가능 수량은 필수 입력 값입니다.")
        AvailableQuantity availableQuantity,

        @Schema(description = "전기 필요 여부", example = "필요", allowableValues = {
                "필요",
                "불필요",
                "논의 필요"
        })
        @NotNull(message = "전기 사용 여부는 필수 입력 값입니다.")
        NeedElectricity needElectricity,

        @Schema(description = "결제 수단", example = "계좌이체", allowableValues = {
                "무관",
                "계좌이체",
                "카드"
        })
        @NotNull(message = "결제 수단은 필수 입력 값입니다.")
        PaymentMethod paymentMethod,

        @Schema(
                description = "운영 가능 날짜 리스트 (형식: \"yyyy.MM.dd ~ yyyy.MM.dd\")",
                example = "[\"2025.10.11 ~ 2025.11.10\", \"2025.11.20 ~ 2025.11.22\"]"
        )
        @Size(min = 1, max = 4, message = "운영 가능 날짜는 최소 1개, 최대 4개까지 등록 가능합니다.")
        List<DateRangeRequest> availableDates,

        @Schema(description = "푸드트럭 사진 URL 리스트", example = "[\"http://image1.png\", \"http://image2.png\"]")
        @Size(min = 1, max = 9, message = "푸드트럭 사진은 최소 1개 최대 9개까지 등록 가능합니다.")
        List<@NotBlank String> photoUrls,

        @Schema(description = "운영 정보", example = "맛있는 음식을 신속하게 제공합니다.")
        String operatingInfo,

        @Schema(description = "기타 옵션", example = "추가 요청 사항이 있으면 기재해주세요.")
        String option
) {
}
