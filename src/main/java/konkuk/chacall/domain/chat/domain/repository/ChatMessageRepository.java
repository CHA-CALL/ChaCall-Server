package konkuk.chacall.domain.chat.domain.repository;

import konkuk.chacall.domain.chat.domain.ChatMessage;
import konkuk.chacall.domain.chat.domain.repository.infra.ChatMessageCustomRepository;
import konkuk.chacall.domain.chat.domain.repository.dto.ChatRoomMetaDataProjection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String>, ChatMessageCustomRepository {
    List<ChatMessage> findByRoomId(Long chatRoomId, PageRequest pageable);

    /**
     * 채팅방 목록에 필요한 집계 데이터
     *  - roomId
     *  - 마지막 메시지 내용
     *  - 마지막 메시지 전송 시간
     *  - 로그인 유저 기준 안 읽은 메시지 개수
     */
    @Aggregation(pipeline = {
            "{ '$match': { 'roomId': { '$in': ?0 } } }",
            "{ '$sort': { 'sendTime': -1 } }",
            "{ '$group': { " +
                    "   '_id': '$roomId'," +
                    "   'roomId': { '$first': '$roomId' }," +
                    "   'lastMessage': { '$first': '$content' }," +
                    "   'lastMessageSendTime': { '$first': '$sendTime' }," +
                    "   'unreadCount': { " +
                    "       '$sum': { " +
                    "           '$cond': [" +
                    "               { '$and': [ { '$eq': ['$read', false] }, { '$ne': ['$senderId', ?1] } ] }," +
                    "               1," +
                    "               0" +
                    "           ]" +
                    "       }" +
                    "   }" +
                    "} }"
    })
    List<ChatRoomMetaDataProjection> aggregateChatRoomSummaries(List<Long> roomIds, Long userId);
}
