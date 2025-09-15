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

    public static OwnerReservationHistoryResponse of(Reservation reservation, User member) {
        List<String> dateTimeList = reservation.getReservationInfo().getFormattedDateTimeInfos();

        return new OwnerReservationHistoryResponse(
                reservation.getReservationId(),
                member.getProfileImageUrl(),
                member.getName(),
                reservation.getReservationInfo().getReservationAddress(),
                dateTimeList
        );
    }
}
