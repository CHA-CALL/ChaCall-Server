package konkuk.chacall.domain.chat.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.chacall.domain.chat.application.ChatService;
import konkuk.chacall.domain.chat.presentation.dto.request.CreateChatRoomRequest;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatOpponentResponse;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatRoomIdResponse;
import konkuk.chacall.global.common.annotation.ExceptionDescription;
import konkuk.chacall.global.common.annotation.UserId;
import konkuk.chacall.global.common.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static konkuk.chacall.global.common.swagger.SwaggerResponseDescription.*;

@Tag(name = "Chat API", description = "채팅 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
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
            @Parameter(description = "채팅방 ID", example = "1") @PathVariable final Long roomId
    ) {
        return BaseResponse.ok(
                chatService.getChatOpponentName(memberId, roomId)
        );
    }


}
