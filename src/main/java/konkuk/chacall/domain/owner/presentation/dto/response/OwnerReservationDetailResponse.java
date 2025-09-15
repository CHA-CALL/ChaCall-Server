package konkuk.chacall.domain.owner.presentation.dto.response;

import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.user.domain.model.User;

import java.util.List;

public record OwnerReservationDetailResponse(
        String userProfileImage,
        String username,
        String address,
        List<String> dateTimeInfos,
        String pdfUrl,
        String menu,
        Integer deposit,
        String electricityInfo,
        String etcRequest
) {
    public static OwnerReservationDetailResponse of(Reservation reservation, User member) {
        List<String> dateTimeList = reservation.getReservationInfo().getFormattedDateTimeInfos();

        // boolean 값을 화면에 표시할 문자열로 변환
        String electricity = reservation.getReservationInfo().isUseElectricity() ? "가능" : "불가능";

        return new OwnerReservationDetailResponse(
                member.getProfileImageUrl(),
                member.getName(),
                reservation.getReservationInfo().getReservationAddress(),
                dateTimeList,
                reservation.getPdfUrl(),
                reservation.getReservationInfo().getMenu(),
                reservation.getReservationInfo().getReservationDeposit(),
                electricity,
                reservation.getReservationInfo().getEtcRequest()
        );
    }
}
