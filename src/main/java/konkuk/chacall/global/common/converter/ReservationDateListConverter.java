package konkuk.chacall.global.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import konkuk.chacall.domain.reservation.domain.value.DateRange;
import konkuk.chacall.domain.reservation.domain.value.ReservationDateList;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DB: "yyyy-MM-dd,yyyy-MM-dd,..." <-> 도메인: ReservationDateList
 */
@Converter(autoApply = false)
public class ReservationDateListConverter implements AttributeConverter<ReservationDateList, String> {

    private static final String ITEM_DELIM = ",";  // 범위 간 구분자
    private static final String RANGE_DELIM = "~"; // 시작/끝 구분자
    private static final DateTimeFormatter DOT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @Override
    public String convertToDatabaseColumn(ReservationDateList attribute) {
        if (attribute == null || attribute.isEmpty()) return "";
        return attribute.toStorageString(); // "yyyy-MM-dd~yyyy-MM-dd,yyyy-MM-dd~yyyy-MM-dd"
    }

    @Override
    public ReservationDateList convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return ReservationDateList.of(List.of());

        String[] parts = dbData.split(ITEM_DELIM);
        List<DateRange> list = new ArrayList<>();

        for (String part : parts) {
            String trimmedPart = part.trim();
            if (trimmedPart.isEmpty()) continue;

            String[] dates = trimmedPart.split(RANGE_DELIM);
            if (dates.length != 2) {
                throw new DomainRuleException(ErrorCode.INVALID_DATE_INPUT);
            }
            LocalDate start = LocalDate.parse(dates[0].trim(), DOT); // ISO yyyy-MM-dd
            LocalDate end   = LocalDate.parse(dates[1].trim(), DOT);
            list.add(new DateRange(start, end));
        }
        return ReservationDateList.of(list);
    }
}