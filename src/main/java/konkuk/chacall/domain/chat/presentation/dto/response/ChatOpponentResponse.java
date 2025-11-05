package konkuk.chacall.domain.chat.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatOpponentResponse(
        @Schema(description = "채팅 상대 이름 (일반 유저 -> 사장님 이름 / 사장님 -> 예약자 이름)", example = "홍길동 or 푸드트럭사장")
        String name,
        @Schema(description = "푸드트럭 이름 (일반 유저 -> null)", example = "맛있는푸드트럭")
        String foodTruckName
) {
    public static ChatOpponentResponse of(String name, String foodTruckName) {
        return new ChatOpponentResponse(name, foodTruckName);
    }
}
