package konkuk.chacall.domain.owner.presentation.dto.response;

import konkuk.chacall.domain.owner.domain.model.ChatTemplate;

public record ChatTemplateResponse(
        Long chatTemplateId,
        String content
) {
    public static ChatTemplateResponse from(ChatTemplate chatTemplate) {
        return new ChatTemplateResponse(
                chatTemplate.getChatTemplateId(),
                chatTemplate.getContent()
        );
    }
}
