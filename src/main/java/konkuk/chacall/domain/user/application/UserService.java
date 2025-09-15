package konkuk.chacall.domain.user.application;

import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.domain.user.domain.repository.UserRepository;
import konkuk.chacall.domain.user.presentation.dto.request.UpdateUserInfoRequest;
import konkuk.chacall.domain.user.presentation.dto.response.GetUserInfoResponse;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public GetUserInfoResponse getUserInfo(Long userId) {
        return userRepository.findById(userId)
                .map(GetUserInfoResponse::from)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updateUserInfo(Long userId, UpdateUserInfoRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        user.update(request.name(), request.profileImageUrl(), request.email(), request.gender(), request.termAgreed());
    }
}
