package konkuk.chacall.domain.user.domain.repository;

import konkuk.chacall.domain.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, User> {
}
