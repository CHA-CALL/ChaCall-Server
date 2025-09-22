package konkuk.chacall.domain.foodtruck.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "조회 기간(시작~종료). 단일 날짜는 start==end")
public record DateRangeRequest(
        @Schema(description = "일정 시작일", example = "2025.09.01")
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDate startDate,
        @Schema(description = "일정 종료일", example = "2025.09.03")
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDate endDate
) {}
