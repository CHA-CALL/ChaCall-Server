package konkuk.chacall.domain.chat.domain.repository.infra;

import konkuk.chacall.domain.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@RequiredArgsConstructor
public class ChatMessageCustomRepositoryImpl implements ChatMessageCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void markMessagesAsReadByUserInRoom(Long userId, Long roomId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomId));
        // 내가 보낸 메시지는 읽음 처리 대상이 아님
        query.addCriteria(Criteria.where("senderId").ne(userId));
        // 아직 읽지 않은 메시지만
        query.addCriteria(Criteria.where("read").is(false));

        Update update = new Update();
        update.set("read", true);

        // 조건에 맞는 모든 문서를 한 번에 업데이트
        mongoTemplate.updateMulti(query, update, ChatMessage.class);
    }
}
