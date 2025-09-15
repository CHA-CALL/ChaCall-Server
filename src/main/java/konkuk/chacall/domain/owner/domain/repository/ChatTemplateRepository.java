package konkuk.chacall.domain.owner.domain.repository;

import konkuk.chacall.domain.owner.domain.model.ChatTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatTemplateRepository extends JpaRepository<ChatTemplate, Long> {
}
