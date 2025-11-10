package konkuk.chacall.domain.chat.application;

import jakarta.validation.Valid;
import konkuk.chacall.domain.chat.application.message.ChatMessageService;
import konkuk.chacall.domain.chat.application.room.ChatRoomService;
import konkuk.chacall.domain.chat.presentation.dto.request.GetChatRoomRequest;
import konkuk.chacall.domain.chat.presentation.dto.request.SendChatMessageRequest;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatMessageResponse;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatOpponentResponse;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatRoomIdResponse;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatRoomResponse;
import konkuk.chacall.domain.member.application.validator.MemberValidator;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.dto.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<ChatMessageResponse> getChatMessages(Long memberId, Long roomId, int page, int size) {
        User user = memberValidator.validateAndGetMember(memberId);

        return chatMessageService.getChatMessages(roomId, user, page, size);
    }

    public CursorPagingResponse<ChatRoomResponse> getChatRooms(Long memberId, GetChatRoomRequest request) {
        User member = memberValidator.validateAndGetMember(memberId);

        CursorPagingRequest cursorPagingRequest = request.pagingOrDefault(SortType.NEWEST);
        return chatRoomService.getChatRooms(member, request.filter(), request.isOwner(), cursorPagingRequest.cursor(), cursorPagingRequest.size());
    }
}
