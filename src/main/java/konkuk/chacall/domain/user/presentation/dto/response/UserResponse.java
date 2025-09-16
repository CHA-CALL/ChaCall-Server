package konkuk.chacall.domain.user.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.user.domain.model.User;

public record UserResponse(
        @Schema(description = "프로필 이미지 URL", example = "http://image.png", nullable = true)
        String profileImageUrl,
        @Schema(description = "이름", example = "홍길동")
        String name,
        @Schema(description = "이메일", example = "email@email.com")
        String email,
        @Schema(description = "성별", example = "남성")
        String gender,
        @Schema(description = "약관 동의 여부", example = "true")
        boolean termAgreed
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getProfileImageUrl(),
                user.getName(),
                user.getEmail(),
                user.getGender() == null ? null : user.getGender().getValue(),
                user.isTermsAgreed()
        );
    }
}
