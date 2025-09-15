package konkuk.chacall.domain.owner.application.chatTemplate;

import konkuk.chacall.domain.owner.domain.model.ChatTemplate;
import konkuk.chacall.domain.owner.domain.repository.ChatTemplateRepository;
import konkuk.chacall.domain.owner.presentation.dto.request.RegisterChatTemplateRequest;
import konkuk.chacall.domain.owner.presentation.dto.request.UpdateChatTemplateRequest;
import konkuk.chacall.domain.owner.presentation.dto.response.ChatTemplateResponse;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChatTemplateService {

    private final ChatTemplateRepository chatTemplateRepository;

    @Transactional
    public void registerChatTemplate(RegisterChatTemplateRequest request, User owner) {
        ChatTemplate chatTemplate = ChatTemplate.of(request.content(), owner);

        chatTemplateRepository.save(chatTemplate);
    }

    public List<ChatTemplateResponse> getChatTemplates(Long ownerId) {
        List<ChatTemplate> chatTemplateList = chatTemplateRepository.findAllByOwnerUserId(ownerId);

        return chatTemplateList.stream()
                .map(ChatTemplateResponse::from)
                .toList();
    }

    @Transactional
    public void updateChatTemplate(UpdateChatTemplateRequest request, Long chatTemplateId) {
        ChatTemplate chatTemplate = chatTemplateRepository.findById(chatTemplateId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CHAT_TEMPLATE_NOT_FOUND));

        chatTemplate.update(
                request.content()
        );
    }

    @Transactional
    public void deleteChatTemplate(Long chatTemplateId) {
        if(!chatTemplateRepository.existsById(chatTemplateId)) {
            throw new EntityNotFoundException(ErrorCode.CHAT_TEMPLATE_NOT_FOUND);
        }

        chatTemplateRepository.deleteById(chatTemplateId);
    }

}
