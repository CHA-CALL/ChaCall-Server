package konkuk.chacall.domain.chat.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CreateChatRoomRequest(
        @Schema(description = "푸드트럭 ID", example = "1")
        @NotNull(message = "푸드트럭 ID는 필수입니다.")
        Long foodTruckId
) {
}
