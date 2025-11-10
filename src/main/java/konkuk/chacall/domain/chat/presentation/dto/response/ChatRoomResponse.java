package konkuk.chacall.domain.chat.presentation.dto.response;

public record ChatRoomResponse(
        Long id,
        String name,
        String foodTruckName,
        String profileImageUrl,
        String lastMessage,
        String lastMessageSendTime,
        long unreadCount
) {
}
