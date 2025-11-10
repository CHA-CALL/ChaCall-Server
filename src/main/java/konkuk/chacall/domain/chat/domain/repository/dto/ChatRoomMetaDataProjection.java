package konkuk.chacall.domain.chat.domain.repository.dto;

import java.time.LocalDateTime;

public interface ChatRoomMetaDataProjection {
    Long getRoomId();

    Long getMemberId();
    Long getOwnerId();

    String getLastMessage();
    LocalDateTime getLastMessageSendTime();

    Long getSortKey();

    Long getUnreadCount();
}
