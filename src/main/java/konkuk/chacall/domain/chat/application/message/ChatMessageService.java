package konkuk.chacall.domain.chat.application.message;

import konkuk.chacall.domain.chat.domain.ChatMessage;
import konkuk.chacall.domain.chat.domain.ChatRoomMetaData;
import konkuk.chacall.domain.chat.domain.repository.ChatMessageRepository;
import konkuk.chacall.domain.chat.domain.repository.ChatRoomMetaDataRepository;
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
    private final ChatRoomMetaDataRepository chatRoomMetaDataRepository;

    public ChatMessageResponse sendMessage(Long roomId, User senderUser, SendChatMessageRequest request) {

        ChatRoomMetaData chatRoomMetaData = chatRoomMetaDataRepository.findByRoomId(roomId)
                .orElseThrow(() -> new EntityNotFoundException(CHAT_ROOM_NOT_FOUND));

        chatRoomMetaData.validateParticipant(senderUser);

        ChatMessage chatMessage = ChatMessage.createChatMessage(
                chatRoomMetaData.getRoomId(),
                senderUser,
                request.content(),
                request.contentType()
        );

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        chatRoomMetaData.updateLastMessage(savedMessage.getContent(), savedMessage.getSendTime());
        chatRoomMetaDataRepository.save(chatRoomMetaData);

        return ChatMessageResponse.from(savedMessage);
    }

    public List<ChatMessageResponse> getChatMessages(Long roomId, User user, int page, int size) {
        ChatRoomMetaData chatRoomMetaData = chatRoomMetaDataRepository.findByRoomId(roomId)
                .orElseThrow(() -> new EntityNotFoundException(CHAT_ROOM_NOT_FOUND));

        chatRoomMetaData.validateParticipant(user);

        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sendTime"));

        return chatMessageRepository.findByRoomId(chatRoomMetaData.getRoomId(), pageable)
                .stream()
                .map(ChatMessageResponse::from)
                .toList();
    }

    public void markMessagesAsRead(User user, Long roomId) {
        ChatRoomMetaData chatRoomMetaData = chatRoomMetaDataRepository.findByRoomId(roomId)
                .orElseThrow(() -> new EntityNotFoundException(CHAT_ROOM_NOT_FOUND));

        chatRoomMetaData.validateParticipant(user);

        chatMessageRepository.markMessagesAsReadByUserInRoom(user.getUserId(), roomId);
    }
}
