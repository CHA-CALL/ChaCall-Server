package konkuk.chacall.domain.user.application;

import konkuk.chacall.domain.user.application.admin.AdminService;
import konkuk.chacall.domain.user.application.validator.AdminValidator;
import konkuk.chacall.domain.user.presentation.dto.request.ApproveFoodTruckStatusRequest;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.domain.user.domain.repository.UserRepository;
import konkuk.chacall.domain.user.presentation.dto.request.UpdateUserInfoRequest;
import konkuk.chacall.domain.user.presentation.dto.response.UserResponse;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import konkuk.chacall.global.common.storage.dto.ImageRequest;
import konkuk.chacall.global.common.storage.dto.ImageResponse;
import konkuk.chacall.global.common.storage.presign.PresignedUrlService;
import konkuk.chacall.global.common.storage.util.KeyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final AdminService adminService;
    private final AdminValidator adminValidator;

    private final PresignedUrlService presignedUrlService;
    private static final int USER_PROFILE_IMAGE_MAX_COUNT = 1;

    public UserResponse getUserInfo(Long userId) {
        return userRepository.findById(userId)
                .map(UserResponse::from)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updateUserInfo(Long userId, UpdateUserInfoRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        user.update(request.name(), request.profileImageUrl(), request.email(), request.gender(), request.termAgreed());
    }

    @Transactional
    public void approveFoodTruckStatus(Long userId, Long foodTruckId, ApproveFoodTruckStatusRequest request) {
        adminValidator.validateAdmin(userId);

        adminService.approveFoodTruckStatus(foodTruckId, request);
    }

    public ImageResponse createUserImagePresignedUrl(Long userId, ImageRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        return presignedUrlService.generatePresignedUrls(
                request,
                user.getUserId(),
                USER_PROFILE_IMAGE_MAX_COUNT,
                KeyUtils::buildUserProfileImageKey
        );
    }
}
