package konkuk.chacall.domain.owner.domain.repository;

import konkuk.chacall.domain.owner.domain.model.ChatTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatTemplateRepository extends JpaRepository<ChatTemplate, Long> {

    List<ChatTemplate> findAllByOwnerUserId(Long ownerId);
}
