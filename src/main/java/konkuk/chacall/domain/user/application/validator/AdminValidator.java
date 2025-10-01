package konkuk.chacall.domain.user.application.validator;

import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.domain.user.domain.repository.UserRepository;
import konkuk.chacall.global.common.annotation.HelperService;
import konkuk.chacall.global.common.domain.BaseStatus;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;

@HelperService
@RequiredArgsConstructor
public class AdminValidator {

    private final UserRepository userRepository;

    public void validateAdmin(Long adminId) {
        User user = userRepository.findByUserIdAndStatus(adminId, BaseStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        user.validateAdmin();
    }
}
