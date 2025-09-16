package konkuk.chacall.domain.owner.application.validator;

import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.domain.user.domain.repository.UserRepository;
import konkuk.chacall.global.common.annotation.HelperService;
import konkuk.chacall.global.common.domain.BaseStatus;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;

@HelperService
@RequiredArgsConstructor
public class OwnerValidator {

    private final UserRepository userRepository;

    public User validateAndGetOwner(Long ownerId) {
        User user = userRepository.findByUserIdAndStatus(ownerId, BaseStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        user.validateOwner();
        return user;
    }
}
