package konkuk.chacall.domain.chat.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatRoomMetaDataResponse(
        @Schema(description = "채팅 상대 이름 (일반 유저 -> 사장님 이름 / 사장님 -> 예약자 이름)", example = "홍길동 or 푸드트럭사장")
        String name,
        @Schema(description = "푸드트럭 이름 (일반 유저 -> null)", example = "맛있는푸드트럭")
        String foodTruckName,
        @Schema(description = "채팅방과 관련된 예약 ID (있는 경우: ID 반환, 없는 경우: null)", example = "1")
        Long reservationId
) {
    public static ChatRoomMetaDataResponse of(String name, String foodTruckName, Long reservationId) {
        return new ChatRoomMetaDataResponse(name, foodTruckName, reservationId);
    }
}
