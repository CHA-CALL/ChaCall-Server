package konkuk.chacall.domain.chat.domain.repository;

import konkuk.chacall.domain.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
