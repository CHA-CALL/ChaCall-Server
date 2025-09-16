package konkuk.chacall.domain.user.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "사용자 정보 수정 요청 DTO")
public record UpdateUserInfoRequest(

        @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
        @NotBlank(message = "프로필 이미지 URL은 비어 있을 수 없습니다.")
        String profileImageUrl,

        @Schema(description = "사용자 이름", example = "홍길동")
        @NotBlank(message = "이름은 비어 있을 수 없습니다.")
        String name,

        @Schema(description = "사용자 이메일", example = "chacall@kokuk.ac.kr")
        @NotBlank(message = "이메일은 비어 있을 수 없습니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @Schema(description = "사용자 성별", example = "남성")
        @NotBlank(message = "성별은 비어 있을 수 없습니다.")
        String gender,

        @Schema(description = "약관 동의 여부", example = "true")
        @NotNull(message = "약관 동의 여부는 비어 있을 수 없습니다.")
        Boolean termAgreed
) {
}
