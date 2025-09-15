package konkuk.chacall.domain.owner.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterChatTemplateRequest(
        @NotBlank(message = "자주 쓰는 채팅을 입력해야합니다.")
        String content
) {
}
