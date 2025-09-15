package konkuk.chacall.domain.owner.domain.model;

import jakarta.persistence.*;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.domain.BaseEntity;
import lombok.*;

@Getter
@Entity
@Table(name = "chat_templates")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatTemplateId;

    @Column(nullable = false, length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    public static ChatTemplate of(String content, User owner) {
        return ChatTemplate.builder()
                .content(content)
                .owner(owner)
                .build();
    }

    public void update(String content) {
        this.content = content;
    }
}
