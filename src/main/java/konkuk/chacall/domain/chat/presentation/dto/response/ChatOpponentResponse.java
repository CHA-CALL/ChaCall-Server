package konkuk.chacall.domain.chat.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatOpponentResponse(
        @Schema(description = "채팅 상대 이름 (일반 유저 -> 푸드트럭 이름 / 사장님 -> 예약자 이름)", example = "홍길동 or 맛있는푸드트럭")
        String name
) {
    public static ChatOpponentResponse of(String name) {
        return new ChatOpponentResponse(name);
    }
}
