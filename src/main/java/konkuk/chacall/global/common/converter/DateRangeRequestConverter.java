package konkuk.chacall.global.common.converter;

import konkuk.chacall.domain.foodtruck.presentation.dto.request.DateRangeRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateRangeRequestConverter implements Converter<String, DateRangeRequest> {

    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @Override
    public DateRangeRequest convert(String source) {
        if (source == null || !source.contains("~"))
            throw new IllegalArgumentException("날짜는 반드시 'yyyy.MM.dd~yyyy.MM.dd' 형식이어야합니다.");

        String[] parts = source.split("~", 2);
        LocalDate start = LocalDate.parse(parts[0].trim(), F);
        LocalDate end   = LocalDate.parse(parts[1].trim(), F);
        return new DateRangeRequest(start, end);
    }
}
