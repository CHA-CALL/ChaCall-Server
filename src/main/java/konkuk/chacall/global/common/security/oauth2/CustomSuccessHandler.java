package konkuk.chacall.global.common.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.chacall.domain.user.domain.value.Role;
import konkuk.chacall.global.common.security.oauth2.tokenstorage.LoginTokenStorage;
import konkuk.chacall.global.common.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import static konkuk.chacall.global.common.security.constant.AuthParameters.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final LoginTokenStorage loginTokenStorage;

    @Value("${server.web-redirect-domain}")
    private String webRedirectUrlDomain;

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        LoginUser loginUser = oAuth2User.getLoginUser();

        String accessToken = jwtUtil.createAccessToken(loginUser.userId());
        String loginTokenKey = UUID.randomUUID().toString();

        if (oAuth2User.roleNonSelected()) { // 아직 유저 역할을 선택하지 않은 경우
            // 유저 역할 정하는 화면으로 리다이렉트
            loginTokenStorage.put(loginTokenKey, Role.NON_SELECTED, accessToken, Duration.ofMinutes(5));      // ttl 5분
            getRedirectStrategy().sendRedirect(request, response, webRedirectUrlDomain + REDIRECT_ROLE_SELECT_URL.getValue() + "?loginTokenKey=" + loginTokenKey);
        } else { // 유저 역할까지 선택한 기존 유저인 경우
            if(loginUser.role() == Role.MEMBER) {
                // 일반 유저 홈 화면으로 리다이렉트
                loginTokenStorage.put(loginTokenKey, Role.MEMBER, accessToken, Duration.ofMinutes(5));      // ttl 5분
                getRedirectStrategy().sendRedirect(request, response, webRedirectUrlDomain + REDIRECT_MEMBER_HOME_URL.getValue() + "?loginTokenKey=" + loginTokenKey);
                return;
            }
            // 사장님 유저 홈 화면으로 리다이렉트
            loginTokenStorage.put(loginTokenKey, Role.OWNER, accessToken, Duration.ofMinutes(5));      // ttl 5분
            getRedirectStrategy().sendRedirect(request, response, webRedirectUrlDomain + REDIRECT_OWNER_HOME_URL.getValue() + "?loginTokenKey=" + loginTokenKey);
        }
    }
}
