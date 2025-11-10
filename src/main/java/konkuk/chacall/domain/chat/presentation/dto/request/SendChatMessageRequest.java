package konkuk.chacall.domain.chat.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.chat.domain.value.MessageContentType;

public record SendChatMessageRequest(
        @Schema(description = "메시지 내용", example = "안녕하세요!")
        String content,
        @Schema(description = "메시지 타입 (TEXT or IMAGE)", example = "TEXT")
        MessageContentType contentType
) {
}
