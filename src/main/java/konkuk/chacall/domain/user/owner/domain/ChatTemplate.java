package konkuk.chacall.domain.user.owner.domain;

import jakarta.persistence.*;
import konkuk.chacall.domain.user.domain.User;
import konkuk.chacall.global.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_templates")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatTemplateId;

    @Column(nullable = false, length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
}
