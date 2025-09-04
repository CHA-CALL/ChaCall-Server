package konkuk.chacall.global.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import konkuk.chacall.domain.reservation.domain.value.ReservationDateList;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DB: "yyyy-MM-dd,yyyy-MM-dd,..." <-> 도메인: ReservationDateList
 */
@Converter(autoApply = false)
public class ReservationDateListConverter implements AttributeConverter<ReservationDateList, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(ReservationDateList attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.getDates().stream()
                .map(LocalDate::toString) // ISO-8601 (yyyy-MM-dd)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public ReservationDateList convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return ReservationDateList.of(List.of());
        }
        List<LocalDate> dates = Arrays.stream(dbData.split(DELIMITER))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(LocalDate::parse) // 기본 ISO-8601 파서
                .collect(Collectors.toList());
        return ReservationDateList.of(dates);
    }
}