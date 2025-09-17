package konkuk.chacall.domain.owner.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.user.domain.model.User;

import java.util.List;

public record OwnerReservationDetailResponse(
        @Schema(description = "상대방(손님)의 프로필 이미지 URL",
                example = "https://image.url/path/profile.jpg")
        String userProfileImage,
        @Schema(description = "상대방의 이름 또는 닉네임",
                example = "김차콜")
        String username,
        @Schema(description = "예약 주소",
                example = "서울 광진구 화양동 123-45")
        String address,
        @Schema(description = "예약 날짜 및 운영 시간 정보 리스트 (최대 2개)",
                example = "[\"2025-09-20 13시~19시\", \"2025-09-21 13시~19시\"]")
        List<String> dateTimeInfos,
        @Schema(description = "견적서 PDF 다운로드 URL (null 일 수 있음)",
                nullable = true,
                example = "https://storage.url/quotes/reservation_123.pdf")
        String pdfUrl,
        @Schema(description = "운영 메뉴",
                example = "핫도그, 국밥, 짜장면")
        String menu,
        @Schema(description = "지불된 예약금액",
                example = "50000원")
        String deposit,
        @Schema(description = "전기 사용 가능 여부",
                example = "가능")
        String electricityInfo,
        @Schema(description = "기타 요청 사항 (null 일 수 있음)",
                nullable = true,
                example = "음식을 많이 주세요, 늦지말아주세요")
        String etcRequest
) {
    public static OwnerReservationDetailResponse of(Reservation reservation, User member) {
        List<String> dateTimeList = reservation.getReservationInfo().getFormattedDateTimeInfos();

        // boolean 값을 화면에 표시할 문자열로 변환

        return new OwnerReservationDetailResponse(
                member.getProfileImageUrl(),
                member.getName(),
                reservation.getReservationInfo().getReservationAddress(),
                dateTimeList,
                reservation.getPdfUrl(),
                reservation.getReservationInfo().getMenu(),
                reservation.getReservationInfo().parsingReservationDeposit(),
                reservation.getReservationInfo().parsingIsUserElectricity(),
                reservation.getReservationInfo().getEtcRequest()
        );
    }
}
