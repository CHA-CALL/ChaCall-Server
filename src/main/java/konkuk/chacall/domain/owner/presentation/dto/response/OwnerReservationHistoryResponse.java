package konkuk.chacall.domain.owner.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.user.domain.model.User;

import java.util.List;

public record OwnerReservationHistoryResponse(
        @Schema(description = "예약 내역 식별자", example = "1")
        Long reservationId,
        @Schema(description = "유저(고객) 프로필 이미지", example = "http://image.png")
        String userProfileImage,
        @Schema(description = "유저(고객) 이름", example = "홍길동")
        String username,
        @Schema(description = "예약 주소", example = "서울 광진구 화양동")
        String address,
        @Schema(description = "예약 날짜 및 운영 시간 정보 리스트 (최대 2개)",
                example = "[\"2025-09-20 13시~19시\", \"2025-09-21 13시~19시\"]")
        List<String> dateTimeInfos
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
