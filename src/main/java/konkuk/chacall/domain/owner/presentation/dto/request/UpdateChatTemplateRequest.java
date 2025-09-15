package konkuk.chacall.domain.owner.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateChatTemplateRequest(
        @NotBlank(message = "자주 쓰는 채팅은 비어있을 수 없습니다.")
        String content
) {
}
