package konkuk.chacall.domain.owner.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateChatTemplateRequest(
        @Schema(description = "자주 쓰는 채팅 내용", example = "저희집은 마라탕이 맛납니다")
        @NotBlank(message = "자주 쓰는 채팅은 비어있을 수 없습니다.")
        String content
) {
}
