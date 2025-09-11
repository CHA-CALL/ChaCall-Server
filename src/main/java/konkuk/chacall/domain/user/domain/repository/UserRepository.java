package konkuk.chacall.domain.user.domain.repository;

import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.domain.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserIdAndRole(Long id, Role role);
}
