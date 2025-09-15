package konkuk.chacall.domain.owner.presentation.dto.response;

import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.user.domain.model.User;

import java.util.List;

public record OwnerReservationHistoryResponse(
        Long reservationId,
        String userProfileImage,
        String username,
        String address,
        List<String> dateTimeInfo
) {

    public static OwnerReservationHistoryResponse of(Reservation reservation) {
        User customer = reservation.getMember();

        // Embedded 객체 내부의 필드에 접근
        List<String> dateTimeList = reservation.getReservationInfo().getReservationDate().getDates().stream()
                .map(date -> date.toString() + " " + reservation.getReservationInfo().getOperationHour())
                .toList();

        return new OwnerReservationHistoryResponse(
                reservation.getReservationId(),
                customer.getProfileImageUrl(),
                customer.getName(),
                reservation.getReservationInfo().getReservationAddress(),
                dateTimeList
        );
    }
}
