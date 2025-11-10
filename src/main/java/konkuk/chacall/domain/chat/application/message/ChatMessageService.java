package konkuk.chacall.domain.chat.application.message;

import konkuk.chacall.domain.chat.domain.ChatMessage;
import konkuk.chacall.domain.chat.domain.ChatRoom;
import konkuk.chacall.domain.chat.domain.repository.ChatMessageRepository;
import konkuk.chacall.domain.chat.domain.repository.ChatRoomRepository;
import konkuk.chacall.domain.chat.presentation.dto.request.SendChatMessageRequest;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatMessageResponse;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static konkuk.chacall.global.common.exception.code.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessageResponse sendMessage(Long roomId, User senderUser, SendChatMessageRequest request) {

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(CHAT_ROOM_NOT_FOUND));

        //todo 채팅방 참여자 검증 (쿼리가 최대 3개 호출되는데 추후 캐싱 고려)
        chatRoom.validateParticipant(senderUser);

        ChatMessage chatMessage = ChatMessage.createChatMessage(
                chatRoom.getChatRoomId(),
                senderUser,
                request.content(),
                request.contentType()
        );

        chatMessageRepository.save(chatMessage);

        return ChatMessageResponse.from(chatMessage);
    }

    public List<ChatMessageResponse> getChatMessages(Long roomId, User user, int page, int size) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(CHAT_ROOM_NOT_FOUND));

        //todo 채팅방 참여자 검증해야되는데.. 캐싱 고려해서 나중에

        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sendTime"));

        return chatMessageRepository.findByRoomId(chatRoom.getChatRoomId(), pageable)
                .stream()
                .map(ChatMessageResponse::from)
                .toList();
    }
}
