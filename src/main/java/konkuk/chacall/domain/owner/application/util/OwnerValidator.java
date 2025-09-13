package konkuk.chacall.domain.owner.application.util;

import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.domain.user.domain.repository.UserRepository;
import konkuk.chacall.global.common.domain.BaseStatus;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerValidator {

    private final UserRepository userRepository;

    public User validateAndGetOwner(Long ownerId) {
        return userRepository.findByUserIdAndRoleAndStatus(ownerId, Role.OWNER, BaseStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
