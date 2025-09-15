package konkuk.chacall.domain.owner.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RegisterChatTemplateRequest(
        @Schema(description = "자주 쓰는 채팅 내용", example = "안녕하세요. 차콜 푸드트럭입니다!")
        @NotBlank(message = "자주 쓰는 채팅을 입력해야합니다.")
        String content
) {
}
