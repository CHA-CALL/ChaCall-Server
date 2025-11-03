package konkuk.chacall.domain.chat.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.chat.domain.ChatRoom;

public record ChatRoomIdResponse(
        @Schema(description = "생성된 채팅방 ID", example = "1")
        Long chatRoomId
) {
    public static ChatRoomIdResponse of(ChatRoom chatRoom) {
        return new ChatRoomIdResponse(chatRoom.getChatRoomId());
    }
}
