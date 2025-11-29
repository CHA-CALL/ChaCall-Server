package konkuk.chacall.domain.chat.domain.repository.infra;

public interface ChatMessageCustomRepository {
    void markMessagesAsReadByUserInRoom(Long userId, Long roomId);
}
