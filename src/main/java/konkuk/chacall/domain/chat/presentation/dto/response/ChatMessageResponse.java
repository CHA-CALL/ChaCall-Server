package konkuk.chacall.domain.chat.presentation.dto.response;

import konkuk.chacall.domain.chat.domain.ChatMessage;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long roomId,
        Long senderId,
        String content,
        String contentType,
        LocalDateTime sendTime,
        boolean read
) {
    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return new ChatMessageResponse(
                chatMessage.getRoomId(),
                chatMessage.getSenderId(),
                chatMessage.getContent(),
                chatMessage.getContentType(),
                chatMessage.getSendTime(),
                chatMessage.isRead()
        );
    }
}
