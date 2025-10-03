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
        @Parameter(
                name = "regionCodes", in = ParameterIn.QUERY,
                description = "지역 코드들 (prefix 검색, 여러 개 OR). 콤마로 구분",
                schema = @Schema(type = "string", example = "11680,41")
        )
        List<Long> regionCodes,

        @Parameter(
                name = "schedules", in = ParameterIn.QUERY,
                description = "운영 가능 기간들(여러 개 OR). 형식: yyyy.MM.dd~yyyy.MM.dd 를 콤마로 구분",
                schema = @Schema(type = "string", example = "2025.09.10~2025.09.11,2025.10.11~2025.10.31")
        )
        List<DateRangeRequest> schedules,

        @Parameter(
                name = "availableQuantity", in = ParameterIn.QUERY,
                description = "수량 기준",
                schema = @Schema(
                        type = "string",
                        example = "150인분 미만",
                        allowableValues = {
                                "50인분 미만",
                                "100인분 미만",
                                "150인분 미만",
                                "200인분 이상",
                                "논의 필요"
                        }
                )
        )
        AvailableQuantity availableQuantity,

        @Parameter(
                name = "categories", in = ParameterIn.QUERY,
                description = "음식 종류(여러 개 OR, CSV). 예) 분식,한식  / 허용값: 한식, 중식, 일식, 양식, 분식, 카페/디저트, 기타",
                schema = @Schema(type = "string", example = "분식,한식")
        )
        List<MenuCategory> categories,

        @Parameter(
                name = "needElectricity", in = ParameterIn.QUERY,
                description = "전기 사용",
                schema = @Schema(
                        type = "string",
                        example = "논의 필요",
                        allowableValues = {"가능", "불가능", "논의 필요"}
                )
        )
        NeedElectricity needElectricity,

        @Parameter(
                name = "paymentMethod", in = ParameterIn.QUERY,
                description = "결제 방법(무관을 선택하면 필터 미적용)",
                schema = @Schema(
                        type = "string",
                        example = "무관",
                        allowableValues = {"무관", "계좌이체", "카드"}
                )
        )
        PaymentMethod paymentMethod,

        @Valid
        CursorPagingRequest cursorPagingRequest
) implements HasPaging {}

