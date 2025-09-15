package konkuk.chacall.domain.owner.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.owner.domain.model.ChatTemplate;

public record ChatTemplateResponse(
        @Schema(description = "자주 쓰는 채팅 식별자", example = "1")
        Long chatTemplateId,
        @Schema(description = "자주 쓰는 채팅 내용", example = "안녕하세요. 차콜 푸드트럭입니다!")
        String content
) {
    public static ChatTemplateResponse from(ChatTemplate chatTemplate) {
        return new ChatTemplateResponse(
                chatTemplate.getChatTemplateId(),
                chatTemplate.getContent()
        );
    }
}
