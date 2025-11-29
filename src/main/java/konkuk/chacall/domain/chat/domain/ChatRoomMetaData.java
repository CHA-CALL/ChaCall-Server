package konkuk.chacall.domain.chat.domain;

import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chat_room_metadata")
@CompoundIndexes({
        // 예약자 기준 목록 조회를 위한 인덱스
        @CompoundIndex(
                name = "member_sort_idx",
                def = "{ 'memberId': 1, 'sortKey': -1 }"
        ),
        // 사장 기준 목록 조회을 위한 인덱스
        @CompoundIndex(
                name = "owner_sort_idx",
                def = "{ 'ownerId': 1, 'sortKey': -1 }"
        )
})
public class ChatRoomMetaData {

    @Id
    private String id;

    // RDB ChatRoom 식별자
    private Long roomId;

    // 참여자 정보 (예약자 / 사장)
    private Long memberId;
    private Long ownerId;

    // 마지막 메시지 정보
    private String lastMessage;
    private LocalDateTime lastMessageSendTime;

    /**
     * 정렬 및 커서용 키
     * - lastMessageSendTime 을 epoch milli 로 변환한 값
     */
    private Long sortKey;

    public static ChatRoomMetaData from(ChatRoom chatRoom) {
        return ChatRoomMetaData.builder()
                .roomId(chatRoom.getChatRoomId())
                .memberId(chatRoom.getMember().getUserId())
                .ownerId(chatRoom.getFoodTruck().getOwner().getUserId())
                .lastMessage(null)
                .lastMessageSendTime(null)
                .sortKey(Long.MAX_VALUE - chatRoom.getChatRoomId()) // 메시지 없는 방은 가장 뒤로 밀리도록 초기값
                .build();
    }

    public void updateLastMessage(String content, LocalDateTime sendTime) {
        this.lastMessage = content;
        this.lastMessageSendTime = sendTime;
        if (sendTime != null) {
            this.sortKey = sendTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
    }

    public void validateParticipant(User user) {
        if(!this.memberId.equals(user.getUserId()) &&
                !this.ownerId.equals(user.getUserId())) {
            throw new DomainRuleException(ErrorCode.CHAT_ROOM_FORBIDDEN);
        }
    }
}