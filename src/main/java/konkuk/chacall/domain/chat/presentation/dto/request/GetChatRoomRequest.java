package konkuk.chacall.domain.chat.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import konkuk.chacall.global.common.dto.HasPaging;

public record GetChatRoomRequest(
        @Schema(description = "채팅방 필터", example = "전체", allowableValues = {"전체", "안 읽음", "예약 확정"})
        @NotNull(message = "채팅방 필터는 null 일 수 없습니다.")
        ChatRoomFilter filter,

        @Schema(description = "현재 채팅방 기준 푸드트럭 사장인지 여부", example = "false")
        @NotNull(message = "isOwner 는 null 일 수 없습니다.")
        Boolean isOwner,

        @Valid
        CursorPagingRequest cursorPagingRequest
) implements HasPaging
{ }
