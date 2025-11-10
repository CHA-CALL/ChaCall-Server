package konkuk.chacall.domain.chat.domain;

import jakarta.persistence.*;
import konkuk.chacall.domain.chat.domain.value.MessageContentType;
import konkuk.chacall.domain.user.domain.model.User;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "chat_messages")
@CompoundIndex(name = "room_time_idx", def = "{'roomId': 1, 'sendTime': 1}")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    private String id;

    private Long roomId;
    private Long senderId;
    private String content;
    private String contentType;

    @Builder.Default
    private LocalDateTime sendTime = LocalDateTime.now();

    @Builder.Default
    private boolean read = false;

    public static ChatMessage createChatMessage(Long roomId, User sender, String content, MessageContentType contentType) {
        return ChatMessage.builder()
                .roomId(roomId)
                .senderId(sender.getUserId())
                .content(content)
                .contentType(contentType.name())
                .build();
    }
}
