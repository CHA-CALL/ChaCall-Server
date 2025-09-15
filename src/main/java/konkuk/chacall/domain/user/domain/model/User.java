package konkuk.chacall.domain.user.domain.model;

import jakarta.persistence.*;
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
    @Column(length = 15, nullable = false)
    private Role role;

    // 약관 동의 여부 컬럼
    @Column(nullable = false)
    private boolean termsAgreed;

    public static User createNewUser(String name, String profileImageUrl, String kakaoId, String email) {
        return User.builder()
                .name(name)
                .profileImageUrl(profileImageUrl)
                .kakaoId(kakaoId)
                .email(email)
                .termsAgreed(false)
                .role(Role.NON_SELECTED)
                .build();
    }

    public void update(String name, String profileImageUrl, String email, String genderStr, boolean termsAgreed) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
        this.gender = Gender.from(genderStr);
        this.termsAgreed = termsAgreed;
    }
}
