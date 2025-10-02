package konkuk.chacall.domain.owner.presentation.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import konkuk.chacall.global.common.dto.HasPaging;
import konkuk.chacall.global.common.dto.SortType;

@Schema(description = "메뉴 목록 조회 요청")
public record MyFoodTruckMenuRequest(
        @Parameter(
                description = "정렬 기준",
                example = "최신순",
                required = false,
                allowEmptyValue = true,
                schema = @Schema(
                        type = "string",
                        allowableValues = {"최신순", "오래된순"},
                        defaultValue = "최신순"
                )
        )
        SortType sort,

        @Valid
        CursorPagingRequest cursorPagingRequest
) implements HasPaging {}

