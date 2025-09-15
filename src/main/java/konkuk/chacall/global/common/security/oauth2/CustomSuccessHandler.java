package konkuk.chacall.global.common.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.global.common.exception.AuthException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import konkuk.chacall.global.common.security.oauth2.tokenstorage.LoginTokenStorage;
import konkuk.chacall.global.common.security.util.JwtUtil;
import konkuk.chacall.global.common.security.property.ServerWebProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static konkuk.chacall.global.common.security.constant.AuthParameters.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final LoginTokenStorage loginTokenStorage;
    private final JwtUtil jwtUtil;
    private final ServerWebProperties serverWebProperties;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        // 1) Resolver에서 세션에 저장한 origin을 복원
        String baseOrigin = null;
        if (request.getSession(false) != null) {
            baseOrigin = (String) request.getSession(false).getAttribute(REDIRECT_SESSION_KEY.getValue());
            request.getSession(false).removeAttribute(REDIRECT_SESSION_KEY.getValue()); // 사용했으면 제거(일회성)
        }

        // 2) 허용 오리진 검증 및 폴백
        if (!serverWebProperties.isAllowed(Objects.toString(baseOrigin, ""))) {
            List<String> origins = serverWebProperties.getWebDomainUrls();
            if (origins == null || origins.isEmpty()) {
                throw new AuthException(ErrorCode.AUTH_WEB_ORIGIN_EMPTY);
            }
            baseOrigin = origins.get(0);
        }

        // 3) 토큰/키 생성
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        LoginUser loginUser = oAuth2User.getLoginUser();
        String accessToken = jwtUtil.createAccessToken(loginUser.userId());
        String loginTokenKey = UUID.randomUUID().toString();

        // 4) 역할별 경로 결정
        String path;
        Role storeRole;
        if (oAuth2User.roleNonSelected()) {
            storeRole = Role.NON_SELECTED;
            path = REDIRECT_ROLE_SELECT_URL.getValue();
        } else if (loginUser.role() == Role.MEMBER) {
            storeRole = Role.MEMBER;
            path = REDIRECT_MEMBER_HOME_URL.getValue();
        } else {
            storeRole = Role.OWNER;
            path = REDIRECT_OWNER_HOME_URL.getValue();
        }

        loginTokenStorage.put(loginTokenKey, storeRole, accessToken, Duration.ofMinutes(5));

        // 5) 최종 리다이렉트
        String redirectUrl = baseOrigin + path + "?loginTokenKey=" + loginTokenKey;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}