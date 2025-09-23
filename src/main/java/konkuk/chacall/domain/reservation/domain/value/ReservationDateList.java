package konkuk.chacall.domain.reservation.domain.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationDateList {

    private static final DateTimeFormatter DOT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private final List<DateRange> ranges;

    public static ReservationDateList of(List<DateRange> ranges) {
        return new ReservationDateList(ranges == null ? List.of() : List.copyOf(ranges));
    }

    public boolean isEmpty() {
        return ranges == null || ranges.isEmpty();
    }

    public List<DateRange> getRanges() {
        return Collections.unmodifiableList(ranges);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ReservationDateList fromJson(List<String> rawRanges) {
        if (rawRanges == null || rawRanges.isEmpty()) return ReservationDateList.of(List.of());

        List<DateRange> list = new ArrayList<>();

        for (String range : rawRanges) {
            if (range == null || range.isBlank()) continue;
            String[] dates = range.split("~");

            if (dates.length != 2) {
                throw new DomainRuleException(ErrorCode.INVALID_DATE_FORMAT);
            }

            LocalDate start = parseDot(dates[0].trim());
            LocalDate end   = parseDot(dates[1].trim());
            list.add(new DateRange(start, end));
        }
        return ReservationDateList.of(list);
    }

    public String toStorageString() {
        if (isEmpty()) return "";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < ranges.size(); i++) {
            DateRange range = ranges.get(i);

            sb.append(range.startDate().format(DOT))
                    .append("~")
                    .append(range.endDate().format(DOT));

            if (i < ranges.size() - 1) sb.append(",");
        }

        return sb.toString();
    }

    private static LocalDate parseDot(String s) {
        try {
            return LocalDate.parse(s, DOT);
        } catch (DateTimeParseException e) {
            throw new DomainRuleException(ErrorCode.INVALID_DATE_FORMAT);
        }
    }
}