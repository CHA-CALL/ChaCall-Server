package konkuk.chacall.domain.chat.application;

import konkuk.chacall.domain.chat.application.room.ChatRoomService;
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

    private final MemberValidator memberValidator;

    @Transactional
    public ChatRoomIdResponse createChatRoom(Long memberId, Long foodTruckId) {
        User member = memberValidator.validateAndGetMember(memberId);

        return chatRoomService.createChatRoom(member, foodTruckId);
    }
}
