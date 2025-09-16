package konkuk.chacall.domain.member.application.validator;

import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.domain.user.domain.repository.UserRepository;
import konkuk.chacall.global.common.annotation.HelperService;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import static konkuk.chacall.global.common.domain.BaseStatus.ACTIVE;
import static konkuk.chacall.global.common.exception.code.ErrorCode.USER_NOT_FOUND;

@HelperService
@RequiredArgsConstructor
public class MemberValidator {

    private final UserRepository userRepository;

    public User validateAndGetMember(Long memberId) {
        User user = userRepository.findByUserIdAndStatus(memberId, ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        user.validateMember();
        return user;
    }
}
