package konkuk.chacall.global.common.converter;

import konkuk.chacall.domain.foodtruck.presentation.dto.request.DateRangeRequest;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class DateRangeRequestConverter implements Converter<String, DateRangeRequest> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @Override
    public DateRangeRequest convert(String source) {
        if (source == null || !source.contains("~"))
            throw new DomainRuleException(ErrorCode.INVALID_DATE_FORMAT);

        try {
            String[] parts = source.split("~", 2);
            LocalDate startDate = LocalDate.parse(parts[0].trim(), FORMATTER);
            LocalDate endDate   = LocalDate.parse(parts[1].trim(), FORMATTER);

            if (endDate.isBefore(startDate)) {
                throw new DomainRuleException(ErrorCode.INVALID_DATE_RANGE);
            }

            return new DateRangeRequest(startDate, endDate);
        } catch (DateTimeParseException e) {
            throw new DomainRuleException(ErrorCode.INVALID_DATE_FORMAT);
        }
    }
}
