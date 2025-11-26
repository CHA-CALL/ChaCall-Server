package konkuk.chacall.domain.chat.domain.repository.infra;

import konkuk.chacall.domain.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@RequiredArgsConstructor
public class ChatMessageCustomRepositoryImpl implements ChatMessageCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void markMessagesAsReadByUserInRoom(Long userId, Long roomId) {
        // 1) 가장 최근의 읽음 메시지 1개 찾기
        Query lastReadQuery = new Query();
        lastReadQuery.addCriteria(Criteria.where("roomId").is(roomId));
        lastReadQuery.addCriteria(Criteria.where("senderId").ne(userId));
        lastReadQuery.addCriteria(Criteria.where("read").is(true));
        lastReadQuery.with(Sort.by(Sort.Direction.DESC, "sendTime"));
        lastReadQuery.limit(1);

        ChatMessage lastReadMessage =
                mongoTemplate.findOne(lastReadQuery, ChatMessage.class);

        Update update = new Update().set("read", true);

        Query updateQuery = new Query();
        updateQuery.addCriteria(Criteria.where("roomId").is(roomId));
        updateQuery.addCriteria(Criteria.where("senderId").ne(userId));
        updateQuery.addCriteria(Criteria.where("read").is(false));

        // 2) 마지막 읽음 메시지가 있다면 sendTime 조건 추가
        if (lastReadMessage != null) {
            updateQuery.addCriteria(Criteria.where("sendTime").gt(lastReadMessage.getSendTime()));
        }

        // 3) 일괄 업데이트
        mongoTemplate.updateMulti(updateQuery, update, ChatMessage.class);
    }
}
