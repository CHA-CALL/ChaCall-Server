package konkuk.chacall.domain.chat.domain.repository;

import konkuk.chacall.domain.chat.domain.ChatMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByRoomId(Long chatRoomId, PageRequest pageable);
}
