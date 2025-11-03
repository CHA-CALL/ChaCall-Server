package konkuk.chacall.domain.chat.domain;

import jakarta.persistence.*;
import konkuk.chacall.domain.chat.domain.value.MessageContentType;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "chat_messages")
@CompoundIndex(name = "room_sender_idx", def = "{'roomId': 1, 'senderId': 1}")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    private Long id;

    private Long roomId;
    private Long senderId;
    private String content;
    private String contentType;
    private LocalDateTime sendTime;
    private boolean read;

    public ChatMessage(Long roomId, Long senderId, String content, String contentType) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.content = content;
        this.contentType = contentType;
        this.sendTime = LocalDateTime.now();
        this.read = false;
    }
}
