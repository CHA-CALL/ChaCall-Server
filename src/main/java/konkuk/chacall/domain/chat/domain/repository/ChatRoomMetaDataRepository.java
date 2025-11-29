package konkuk.chacall.domain.chat.domain.repository;

import konkuk.chacall.domain.chat.domain.ChatRoomMetaData;
import konkuk.chacall.domain.chat.domain.repository.dto.ChatRoomMetaDataProjection;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMetaDataRepository extends MongoRepository<ChatRoomMetaData, String> {
    Optional<ChatRoomMetaData> findByRoomId(Long roomId);

    /**
     * - 자신의 채팅방만 필터링
     * - 최근 메시지 시간 기준 내림차순 정렬
     * - 커서(sortKey) 기반 페이지네이션
     * - 안읽은 메시지 개수까지 함께 조회
     */
    @Aggregation(pipeline = {
            // 1. 내가 속한 채팅방만 필터링 (isOwner 기준)
            "{ '$match': { '$expr': { '$and': [" +
                    "  { '$cond': [ ?1, { '$eq': ['$ownerId', ?0] }, { '$eq': ['$memberId', ?0] } ] }," +
                    "  { '$lt': ['$sortKey', ?2] }" +
                    "] } } }",

            // 2. 안읽은 메시지 개수 계산 (chat_messages 컬렉션 join)
            "{ '$lookup': { " +
                    "   'from': 'chat_messages'," +
                    "   'let': { 'roomId': '$roomId' }," +
                    "   'pipeline': [" +
                    "       { '$match': { '$expr': { '$and': [" +
                    "           { '$eq': ['$roomId', '$$roomId'] }," +
                    "           { '$eq': ['$read', false] }," +
                    "           { '$ne': ['$senderId', ?0] }" +
                    "       ] } } }," +
                    "       { '$count': 'unreadCount' }" +
                    "   ]," +
                    "   'as': 'unreadInfo'" +
                    "} }",

            // 3. unreadInfo 배열 -> unreadCount 필드로 변환 (없으면 0)
            "{ '$addFields': { " +
                    "   'unreadCount': { '$ifNull': [ { '$arrayElemAt': ['$unreadInfo.unreadCount', 0] }, 0 ] }" +
                    "} }",

            // 4. sortKey 기준 내림차순 정렬 (가장 최근 대화가 위로)
            "{ '$sort': { 'sortKey': -1 } }",

            // 5. 페이지 사이즈 + 1 만큼만 가져와서 hasNext 판단
            "{ '$limit': ?3 }"
    })
    List<ChatRoomMetaDataProjection> findChatRoomsForUser(Long userId, boolean isOwner, Long cursorSortKey, int limit);
}
