package konkuk.chacall.domain.chat.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.chat.domain.ChatRoom;
import konkuk.chacall.domain.chat.domain.repository.dto.ChatRoomMetaDataProjection;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.util.DateUtil;

import java.util.Optional;

public record ChatRoomResponse(
        @Schema(description = "채팅방 ID", example = "1")
        Long id,
        @Schema(description = "상대방 이름", example = "홍길동")
        String name,
        @Schema(description = "푸드트럭 이름", example = "맛있는 푸드트럭")
        String foodTruckName,
        @Schema(description = "상대방 프로필 이미지 URL", example = "https://example.com/profile.jpg")
        String profileImageUrl,
        @Schema(description = "마지막 메시지 내용", example = "안녕하세요!")
        String lastMessage,
        @Schema(description = "마지막 메시지 전송 시간", example = "오후 5:49 or 어제 or 9월 30일 or 2023년 10월")
        String lastMessageSendTime,
        long unreadCount
) {

    public static ChatRoomResponse from(ChatRoom chatRoom, ChatRoomMetaDataProjection meta, boolean isOwner) {
        // 현재 뷰 기준 상대방 정보
        User oppenent = isOwner ? chatRoom.getMember() : chatRoom.getFoodTruck().getOwner();
        String name = oppenent.getName();

        String foodTruckName = chatRoom.getFoodTruck().getFoodTruckInfo().getName();
        String profileImageUrl = oppenent.getProfileImageUrl();

        String lastMessage = meta.getLastMessage();
        String lastMessageSendTime = (meta.getLastMessageSendTime() != null)
                ? DateUtil.formatLocalDateTime(meta.getLastMessageSendTime())
                : null;

        long unreadCount = Optional.ofNullable(meta.getUnreadCount()).orElse(0L);

        return new ChatRoomResponse(
                chatRoom.getChatRoomId(),
                name,
                foodTruckName,
                profileImageUrl,
                lastMessage,
                lastMessageSendTime,
                unreadCount
        );
    }
}
