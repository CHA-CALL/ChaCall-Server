package konkuk.chacall.domain.chat.application;

import konkuk.chacall.domain.chat.application.message.ChatMessageService;
import konkuk.chacall.domain.chat.application.room.ChatRoomService;
import konkuk.chacall.domain.chat.presentation.dto.request.SendChatMessageRequest;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatMessageResponse;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatOpponentResponse;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatRoomIdResponse;
import konkuk.chacall.domain.member.application.validator.MemberValidator;
import konkuk.chacall.domain.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    private final MemberValidator memberValidator;

    @Transactional
    public ChatRoomIdResponse createChatRoom(Long memberId, Long foodTruckId) {
        User member = memberValidator.validateAndGetMember(memberId);

        return chatRoomService.createChatRoom(member, foodTruckId);
    }

    public ChatOpponentResponse getChatOpponentName(Long memberId, Long roomId, boolean isOwner) {
        User user = memberValidator.validateAndGetMember(memberId);

        return chatRoomService.getChatOpponentName(user, roomId, isOwner);
    }

    public ChatMessageResponse sendMessage(Long roomId, Long userId, SendChatMessageRequest request) {
        User senderUser = memberValidator.validateAndGetMember(userId);

        return chatMessageService.sendMessage(roomId, senderUser, request);
    }
}
