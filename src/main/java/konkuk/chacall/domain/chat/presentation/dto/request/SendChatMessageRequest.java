package konkuk.chacall.domain.chat.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import konkuk.chacall.domain.chat.domain.value.MessageContentType;

public record SendChatMessageRequest(
        @Schema(description = "메시지 내용", example = "안녕하세요!")
        @NotBlank(message = "메시지 내용은 필수입니다.")
        String content,
        @Schema(description = "메시지 타입 (TEXT or IMAGE)", example = "TEXT")
        @NotNull(message = "메시지 타입은 필수입니다.")
        MessageContentType contentType
) {
}
