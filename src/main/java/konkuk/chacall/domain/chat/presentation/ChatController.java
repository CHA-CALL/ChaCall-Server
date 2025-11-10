package konkuk.chacall.domain.chat.presentation;

import io.swagger.v3.oas.annotations.Parameter;
import konkuk.chacall.domain.chat.application.ChatService;
import konkuk.chacall.domain.chat.presentation.dto.request.SendChatMessageRequest;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatMessageResponse;
import konkuk.chacall.global.common.annotation.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/rooms/{roomId}") // /pub/rooms/{roomId}
    public void sendMessage(
            @DestinationVariable Long roomId,
            @Parameter(hidden = true) @UserId final Long userId,
            SendChatMessageRequest request
    ){
        ChatMessageResponse chatMessageResponse = chatService.sendMessage(roomId, userId, request);
        messagingTemplate.convertAndSend("/sub/rooms/" + roomId, chatMessageResponse);
    }
}
