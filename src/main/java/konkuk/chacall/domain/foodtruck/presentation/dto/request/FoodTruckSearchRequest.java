package konkuk.chacall.domain.foodtruck.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import konkuk.chacall.domain.foodtruck.domain.value.AvailableQuantity;
import konkuk.chacall.domain.foodtruck.domain.value.MenuCategory;
import konkuk.chacall.domain.foodtruck.domain.value.NeedElectricity;
import konkuk.chacall.domain.foodtruck.domain.value.PaymentMethod;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import konkuk.chacall.global.common.dto.HasPaging;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "푸드트럭 검색/필터 요청")
public record FoodTruckSearchRequest(
        @Schema(description = "검색어(이름/설명 LIKE)", example = "디저트")
        String keyword,
        @Schema(description = "지역 코드 (prefix 검색)", example = "11680")
        Long regionCode,
        @Schema(description = "운영 가능해야 하는 후보 기간들 (여러 개 OR 매칭)")
        List<DateRangeRequest> schedules,
        @Schema(description = "수량 기준", example = "LESS_THAN_150")
        AvailableQuantity availableQuantity,
        @Schema(description = "음식 종류(여러개 OR 매칭)", example = "[\"디저트\",\"분식\"]")
        List<MenuCategory> categories,
        @Schema(description = "전기 사용", example = "REQUIRED")
        NeedElectricity needElectricity,
        @Schema(description = "결제 방법(ANY 를 선택하면 필터 미적용)", example = "CARD")
        PaymentMethod paymentMethod,

        @Valid
        CursorPagingRequest cursorPagingRequest
) implements HasPaging {
}
