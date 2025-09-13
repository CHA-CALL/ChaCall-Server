package konkuk.chacall.domain.chat.domain;

import jakarta.persistence.*;
import konkuk.chacall.domain.chat.domain.value.MessageContentType;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long chatMessageId;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime sendTime;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User senderUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageContentType contentType;

}
