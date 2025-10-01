package konkuk.chacall.global.common.storage.util;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class KeyUtils {

    private final static String RESERVATION_ESTIMATE_PREFIX = "reservations";
    private final static DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    public static String buildReservationPdfKey(Long reservationId) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER); // ex) 20250923-163045
        return String.format("%s/%s/%d/estimate.pdf",
                RESERVATION_ESTIMATE_PREFIX,
                timestamp,
                reservationId);
    }

    public static String buildFoodTruckImageKey(Long userId) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER); // ex) 20250923-163045
        String uuid = UUID.randomUUID().toString();
        return String.format("foodtrucks/%d/%s/%s",
                userId,
                timestamp,
                uuid); // foodtrucks/{userId}/{timestamp}/{uuid}
    }

    public static String buildMenuImageKey(Long foodTruckId) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER); // ex) 20250923-163045
        String uuid = UUID.randomUUID().toString();
        return String.format("menus/%d/%s/%s",
                foodTruckId,
                timestamp,
                uuid); // menus/{foodTruckId}/{timestamp}/{uuid}
    }
}