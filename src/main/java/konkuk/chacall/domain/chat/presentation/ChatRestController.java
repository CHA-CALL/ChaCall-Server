package konkuk.chacall.domain.chat.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import konkuk.chacall.domain.chat.application.ChatService;
import konkuk.chacall.domain.chat.presentation.dto.request.CreateChatRoomRequest;
import konkuk.chacall.domain.chat.presentation.dto.request.GetChatRoomRequest;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatMessageResponse;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatOpponentResponse;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatRoomIdResponse;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatRoomResponse;
import konkuk.chacall.global.common.annotation.ExceptionDescription;
import konkuk.chacall.global.common.annotation.UserId;
import konkuk.chacall.global.common.dto.BaseResponse;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static konkuk.chacall.global.common.swagger.SwaggerResponseDescription.*;

@Tag(name = "Chat API", description = "채팅 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRestController {

    private final ChatService chatService;

    @Operation(
            summary = "채팅방 생성 (채팅 시작)",
            description = "예약자와 사장님간의 채팅방을 생성합니다."
    )
    @ExceptionDescription(CREATE_CHAT_ROOM)
    @PostMapping("/rooms")
    public BaseResponse<ChatRoomIdResponse> createChatRoom(
            @Parameter(hidden = true) @UserId final Long memberId,
            @RequestBody @Valid final CreateChatRoomRequest request
    ) {
        return BaseResponse.ok(
                chatService.createChatRoom(memberId, request.foodTruckId())
        );
    }

    @Operation(
            summary = "채팅 상대 이름 조회",
            description = "채팅 상단에 표시되는 채팅 상대의 이름을 조회합니다."
    )
    @ExceptionDescription(GET_CHAT_OPPONENT_NAME)
    @GetMapping("/rooms/{roomId}")
    public BaseResponse<ChatOpponentResponse> getChatOpponentName(
            @Parameter(hidden = true) @UserId final Long memberId,
            @Parameter(description = "채팅방 ID", example = "1") @PathVariable final Long roomId,
            @Parameter(description = "현재 채팅방 기준 푸드트럭 사장인지 여부", example = "false")
            @RequestParam final Boolean isOwner
    ) {
        return BaseResponse.ok(
                chatService.getChatOpponentName(memberId, roomId, isOwner)
        );
    }

    @Operation(
            summary = "메시지 내역 조회",
            description = "특정 채팅방의 메시지 내역을 조회합니다."
    )
    @GetMapping("/rooms/{roomId}/messages")
    public BaseResponse<List<ChatMessageResponse>> getChatMessages(
            @Parameter(hidden = true) @UserId final Long userId,
            @Parameter(description = "채팅방 ID", example = "1") @PathVariable final Long roomId,
            @Parameter(description = "페이지 번호", example = "0") @RequestParam @NotNull(message = "페이지 번호는 필수입니다.")
            final Integer page,
            @Parameter(description = "페이지 크기", example = "20") @RequestParam @NotNull(message = "페이지 크기는 필수입니다.")
            final Integer size
    ) {
        return BaseResponse.ok(
                chatService.getChatMessages(userId, roomId, page, size)
        );
    }

    @Operation(
            summary = "채팅방 목록 조회",
            description = "사용자가 속한 채팅방 목록을 조회합니다."
    )
    @GetMapping("/rooms")
    public BaseResponse<CursorPagingResponse<ChatRoomResponse>> getChatRooms(
            @Parameter(hidden = true) @UserId final Long userId,
            @Valid @ParameterObject final GetChatRoomRequest request
    ) {
        return BaseResponse.ok(
                chatService.getChatRooms(userId, request)
        );
    }

    @Operation(
            summary = "채팅방 내 메시지 읽음 처리"
    )
    @PatchMapping("/rooms/{roomId}/read")
    public BaseResponse<Void> markMessagesAsRead(
            @Parameter(hidden = true) @UserId final Long userId,
            @Parameter(description = "채팅방 ID", example = "1") @PathVariable final Long roomId
    ) {
        chatService.markMessagesAsRead(userId, roomId);
        return BaseResponse.ok(null);
    }
}
