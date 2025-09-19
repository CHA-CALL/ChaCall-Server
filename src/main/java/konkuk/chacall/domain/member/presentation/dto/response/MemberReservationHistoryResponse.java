package konkuk.chacall.domain.member.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import konkuk.chacall.domain.reservation.domain.model.Reservation;

import java.util.List;

public record MemberReservationHistoryResponse(
        @Schema(description = "예약 내역 식별자", example = "1")
        Long reservationId,
        @Schema(description = "푸드트럭 대표 사진 URL", example = "https://example.com/foodtruck.jpg")
        String photoUrl,
        @Schema(description = "푸드트럭 이름", example = "맛있는 푸드트럭")
        String name,
        @Schema(description = "예약 주소", example = "서울 광진구 화양동")
        String address,
        @Schema(description = "예약 날짜 및 운영 시간 정보 리스트 (최대 2개)",
                example = "[\"2025-09-20 13시~19시\", \"2025-09-21 13시~19시\"]")
        List<String> dateTimeInfos
) {

    public static MemberReservationHistoryResponse of(Reservation reservation, FoodTruck foodTruck) {
        List<String> dateTimeList = reservation.getReservationInfo().getFormattedDateTimeInfos();

        return new MemberReservationHistoryResponse(
                reservation.getReservationId(),
                foodTruck.getFoodTruckPhotoList().getMainPhotoUrl(),
                foodTruck.getName(),
                reservation.getReservationInfo().getFullAddress(),
                dateTimeList
        );
    }
}

