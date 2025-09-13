package konkuk.chacall.domain.user.domain;

import jakarta.persistence.*;
import konkuk.chacall.domain.user.domain.value.Gender;
import konkuk.chacall.domain.user.domain.value.Role;
import konkuk.chacall.global.common.domain.BaseEntity;
import lombok.*;

@Getter
@Entity
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private String profileImageUrl;

    @Column(nullable = false, unique = true)
    private String kakaoId;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Role role;

    public static User createNewUser(String name, String profileImageUrl, String kakaoId, String email) {
        return User.builder()
                .name(name)
                .profileImageUrl(profileImageUrl)
                .kakaoId(kakaoId)
                .email(email)
                .role(Role.NON_SELECTED)
                .build();
    }
}
