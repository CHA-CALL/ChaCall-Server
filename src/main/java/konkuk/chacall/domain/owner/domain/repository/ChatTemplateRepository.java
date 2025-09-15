package konkuk.chacall.domain.owner.domain.repository;

import konkuk.chacall.domain.owner.domain.model.ChatTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatTemplateRepository extends JpaRepository<ChatTemplate, Long> {

    @Query("SELECT ct FROM ChatTemplate ct WHERE ct.owner.userId = :ownerId")
    List<ChatTemplate> findAllByOwnerId(@Param("ownerId") Long ownerId);

}
