package konkuk.chacall.domain.user.domain.repository;

import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.domain.BaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserIdAndRoleAndStatus(Long userId, Role role, BaseStatus status);

    boolean existsByUserIdAndRoleAndStatus(Long userId, Role role, BaseStatus status);
}
