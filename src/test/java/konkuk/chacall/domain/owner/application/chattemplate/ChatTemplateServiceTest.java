package konkuk.chacall.domain.owner.application.chattemplate;

import konkuk.chacall.domain.owner.domain.model.ChatTemplate;
import konkuk.chacall.domain.owner.domain.repository.ChatTemplateRepository;
import konkuk.chacall.domain.owner.presentation.dto.request.RegisterChatTemplateRequest;
import konkuk.chacall.domain.owner.presentation.dto.request.UpdateChatTemplateRequest;
import konkuk.chacall.domain.owner.presentation.dto.response.ChatTemplateResponse;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatTemplateService 테스트")
class ChatTemplateServiceTest {

    @InjectMocks
    private ChatTemplateService chatTemplateService;

    @Mock
    private ChatTemplateRepository chatTemplateRepository;

    @Mock
    private User owner;

    @Mock
    private ChatTemplate chatTemplate;

    @Nested
    @DisplayName("채팅 템플릿 등록 시나리오")
    class RegisterChatTemplateScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상적인 요청 시 채팅 템플릿을 성공적으로 등록한다")
            void registerTemplateSuccess() {
                // given
                RegisterChatTemplateRequest request = new RegisterChatTemplateRequest("안녕하세요.");
                given(chatTemplateRepository.save(any(ChatTemplate.class))).willReturn(chatTemplate);

                // when
                chatTemplateService.registerChatTemplate(request, owner);

                // then
                verify(chatTemplateRepository).save(any(ChatTemplate.class));
            }
        }
    }

    @Nested
    @DisplayName("채팅 템플릿 목록 조회 시나리오")
    class GetChatTemplatesScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("소유주의 ID로 채팅 템플릿 목록을 성공적으로 조회한다")
            void getTemplatesSuccess() {
                // given
                given(owner.getUserId()).willReturn(1L);
                given(chatTemplateRepository.findAllByOwnerId(anyLong())).willReturn(Collections.singletonList(chatTemplate));

                // when
                List<ChatTemplateResponse> responses = chatTemplateService.getChatTemplates(owner.getUserId());

                // then
                assertThat(responses).hasSize(1);
                verify(chatTemplateRepository).findAllByOwnerId(anyLong());
            }
        }
    }

    @Nested
    @DisplayName("채팅 템플릿 수정 시나리오")
    class UpdateChatTemplateScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("존재하는 템플릿 ID로 내용을 성공적으로 수정한다")
            void updateTemplateSuccess() {
                // given
                UpdateChatTemplateRequest request = new UpdateChatTemplateRequest("반갑습니다.");
                given(chatTemplateRepository.findById(anyLong())).willReturn(Optional.of(chatTemplate));
                doNothing().when(chatTemplate).update(anyString());

                // when
                chatTemplateService.updateChatTemplate(request, 1L);

                // then
                verify(chatTemplateRepository).findById(anyLong());
                verify(chatTemplate).update(anyString());
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 템플릿 ID로 요청 시 CHAT_TEMPLATE_NOT_FOUND 예외를 발생시킨다")
            void updateWithInvalidTemplateIdFail() {
                // given
                UpdateChatTemplateRequest request = new UpdateChatTemplateRequest("반갑습니다.");
                given(chatTemplateRepository.findById(anyLong())).willReturn(Optional.empty());

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> chatTemplateService.updateChatTemplate(request, 99L));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.CHAT_TEMPLATE_NOT_FOUND);
            }
        }
    }

    @Nested
    @DisplayName("채팅 템플릿 삭제 시나리오")
    class DeleteChatTemplateScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("존재하는 템플릿 ID로 성공적으로 삭제한다")
            void deleteTemplateSuccess() {
                // given
                given(chatTemplateRepository.existsById(anyLong())).willReturn(true);
                doNothing().when(chatTemplateRepository).deleteById(anyLong());

                // when
                chatTemplateService.deleteChatTemplate(1L);

                // then
                verify(chatTemplateRepository).existsById(anyLong());
                verify(chatTemplateRepository).deleteById(anyLong());
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 템플릿 ID로 요청 시 CHAT_TEMPLATE_NOT_FOUND 예외를 발생시킨다")
            void deleteWithInvalidTemplateIdFail() {
                // given
                given(chatTemplateRepository.existsById(anyLong())).willReturn(false);

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> chatTemplateService.deleteChatTemplate(99L));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.CHAT_TEMPLATE_NOT_FOUND);
            }
        }
    }
}
