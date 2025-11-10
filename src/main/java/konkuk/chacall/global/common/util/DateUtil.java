package konkuk.chacall.global.common.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class DateUtil {
    // LocalDateTime을 "오후 hh:mm" 형식의 문자열로 변환하는 메서드 (하루가 지난 경우 "어제" 이틀 이상이 지난 경우 "MM월 dd일" 1년 이상이 지난 경우 "yyyy년 MM월" 형식)
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);

        if (duration.toDays() < 1) {
            // 오늘
            int hour = dateTime.getHour();
            String period = (hour >= 12) ? "오후" : "오전";
            hour = (hour > 12) ? hour - 12 : hour;
            if (hour == 0) hour = 12; // 12시 처리
            int minute = dateTime.getMinute();
            return String.format("%s %d:%02d", period, hour, minute);
        } else if (duration.toDays() < 2) {
            // 어제
            return "어제";
        } else if (duration.toDays() < 365) {
            // 올해
            return String.format("%d월 %d일", dateTime.getMonthValue(), dateTime.getDayOfMonth());
        } else {
            // 1년 이상
            return String.format("%d년 %d월", dateTime.getYear(), dateTime.getMonthValue());
        }
    }

}
