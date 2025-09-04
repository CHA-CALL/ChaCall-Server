package konkuk.chacall.domain.reservation.domain.value;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationDateList {

    private final List<LocalDate> dates;

    public static ReservationDateList of(List<LocalDate> dates) {
        return new ReservationDateList(dates == null ? List.of() : List.copyOf(dates));
    }

    public List<LocalDate> getDates() {
        return Collections.unmodifiableList(dates);
    }

    public boolean contains(LocalDate date) {
        return dates != null && dates.contains(date);
    }

    public int size() {
        return dates.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}