package konkuk.chacall.global.common.security.oauth2;

import konkuk.chacall.domain.user.domain.User;
import konkuk.chacall.domain.user.domain.repository.UserRepository;
import konkuk.chacall.global.common.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static konkuk.chacall.global.common.exception.code.ErrorCode.AUTH_UNSUPPORTED_SOCIAL_LOGIN;
import static konkuk.chacall.global.common.security.constant.AuthParameters.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("KakaoUser: {}", oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserDetails oAuth2UserDetails = null;
        if (registrationId.equals(KAKAO.getValue())) {
            oAuth2UserDetails = new KakaoUserDetails(oAuth2User.getAttributes());
        }
        else {
            log.warn("카카오 로그인만 지원합니다.");
            throw new AuthException(AUTH_UNSUPPORTED_SOCIAL_LOGIN);
        }

        String kakaoId = oAuth2UserDetails.getProvider() + "_" + oAuth2UserDetails.getProviderId(); //kakao_1234567890
        String email = oAuth2UserDetails.getEmail();
        String nickname = oAuth2UserDetails.getNickname();
        String profileImage = oAuth2UserDetails.getProfileImage();

        CustomOAuth2User customOAuth2User = userRepository.findByKakaoId(kakaoId)
                .map(existingUser -> new CustomOAuth2User(
                        LoginUser.createLoginUser(kakaoId, existingUser.getUserId(), existingUser.getRole()
                        )
                ))
                .orElseGet(() -> {
                    User newUser = userRepository.save(User.createNewUser(email, nickname, profileImage, kakaoId));
                    return new CustomOAuth2User(LoginUser.createLoginUser(kakaoId, newUser.getUserId(), newUser.getRole()));
                });

        return customOAuth2User;
    }
}

