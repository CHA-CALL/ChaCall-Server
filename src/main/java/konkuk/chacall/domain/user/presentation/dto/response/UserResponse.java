package konkuk.chacall.domain.user.presentation.dto.response;

import konkuk.chacall.domain.user.domain.model.User;

public record UserResponse(
        String profileImageUrl,
        String name,
        String email,
        String gender,
        boolean termAgreed
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getProfileImageUrl(),
                user.getName(),
                user.getEmail(),
                user.getGender() == null ? null : user.getGender().getValue(),
                user.isTermsAgreed()
        );
    }
}
