package konkuk.chacall.domain.foodtruck.presentation.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import konkuk.chacall.domain.foodtruck.domain.value.AvailableQuantity;
import konkuk.chacall.domain.foodtruck.domain.value.MenuCategory;
import konkuk.chacall.domain.foodtruck.domain.value.NeedElectricity;
import konkuk.chacall.domain.foodtruck.domain.value.PaymentMethod;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import konkuk.chacall.global.common.dto.HasPaging;

import java.util.List;

@Schema(description = "푸드트럭 검색/필터 요청")
public record FoodTruckSearchRequest(
        @Schema(description = "검색어(이름/설명 LIKE)", example = "디저트")
        String keyword,
        @Schema(description = "지역 코드 (prefix 검색)", example = "11680")
        Long regionCode,
        @Parameter(
                name = "schedules", in = ParameterIn.QUERY,
                description = "운영 가능 기간들(여러 개 OR). 형식: yyyy.MM.dd~yyyy.MM.dd 를 콤마로 구분",
                schema = @Schema(
                        type = "string",
                        example = "2025.09.10~2025.09.11,2025.10.11~2025.10.31")
        )
        List<DateRangeRequest> schedules,
        @Schema(description = "수량 기준", example = "LESS_THAN_150")
        AvailableQuantity availableQuantity,
        @Parameter(
                name = "categories", in = ParameterIn.QUERY,
                description = "음식 종류(여러 개 OR, 영어 라벨). 콤마로 구분",
                schema = @Schema(
                        type = "string",
                        example = "SNACK,KOREAN",
                        description = "가능 값: KOREAN, CHINESE, JAPANESE, WESTERN, SNACK, CAFE_DESSERT, ETC"
                )
        )
        List<MenuCategory> categories,
        @Schema(description = "전기 사용", example = "REQUIRED")
        NeedElectricity needElectricity,
        @Schema(description = "결제 방법(ANY 를 선택하면 필터 미적용)", example = "CARD")
        PaymentMethod paymentMethod,

        @Valid
        CursorPagingRequest cursorPagingRequest
) implements HasPaging {
}
