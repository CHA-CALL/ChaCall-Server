package konkuk.chacall.domain.chat.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatRoomFilter {
    ALL("전체"),
    UNREAD("안 읽음"),
    CONFIRMED("예약 확정")
    ;

    private final String value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ChatRoomFilter from(String value) {
        for (ChatRoomFilter filter : ChatRoomFilter.values()) {
            if (filter.getValue().equals(value)) {
                return filter;
            }
        }
        throw new BusinessException(ErrorCode.CHAT_ROOM_FILTER_MISMATCH,
                new IllegalArgumentException("현재 지원하지 않는 채팅방 필터 값입니다: " + value));
    }
}
