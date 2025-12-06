package konkuk.chacall.domain.foodtruck.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Schema(description = "조회 기간(시작~종료). 단일 날짜는 start==end")
public record DateRangeRequest(
        @Schema(description = "일정 시작일", example = "2025.09.01")
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDate startDate,
        @Schema(description = "일정 종료일", example = "2025.09.03")
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDate endDate
) {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        // 문자열 전체를 받아서 DateRangeRequest 로 변환
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public DateRangeRequest(String value) {
                this(parseStart(value), parseEnd(value));
        }

        private static LocalDate parseStart(String value) {
                String[] parts = splitRange(value);
                return LocalDate.parse(parts[0].trim(), FORMATTER);
        }

        private static LocalDate parseEnd(String value) {
                String[] parts = splitRange(value);
                return LocalDate.parse(parts[1].trim(), FORMATTER);
        }

        private static String[] splitRange(String value) {
                if (value == null) {
                        throw new DomainRuleException(ErrorCode.INVALID_DATE_FORMAT,
                                new IllegalArgumentException("날짜 구간 값이 null 일 수 없습니다."));
                }

                String[] parts = value.split("~", 2);
                if (parts.length != 2) {
                        throw new DomainRuleException(ErrorCode.INVALID_DATE_FORMAT,
                                new IllegalArgumentException("날짜 구간 값은 'yyyy.MM.dd ~ yyyy.MM.dd' 형식이어야 합니다."));
                }

                return parts;
        }

}
