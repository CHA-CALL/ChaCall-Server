package konkuk.chacall.global.common.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.chacall.global.common.exception.AuthException;
import konkuk.chacall.global.common.security.oauth2.CustomOAuth2User;
import konkuk.chacall.global.common.security.oauth2.LoginUser;
import konkuk.chacall.global.common.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static konkuk.chacall.global.common.exception.code.ErrorCode.*;
import static konkuk.chacall.global.common.security.constant.AuthParameters.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractToken(request);
            validateToken(token);

            request.setAttribute(JWT_TOKEN_ATTRIBUTE.getValue(), token);
            LoginUser loginUser = jwtUtil.getLoginUser(token);

            request.setAttribute(JWT_ACCESS_TOKEN_KEY.getValue(), loginUser.userId());

            CustomOAuth2User customOAuth2User = new CustomOAuth2User(loginUser);

            Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (Exception e) {
            log.error("JWT 필터에서 오류 발생: {}", e.getMessage());
            request.setAttribute("exception", e);
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private void validateToken(String token) {
        if (token == null) {
            throw new AuthException(AUTH_TOKEN_NOT_FOUND);
        }

        if (!jwtUtil.validateToken(token)) {
            throw new AuthException(AUTH_INVALID_TOKEN);
        }

        if (jwtUtil.isExpired(token)) {
            throw new AuthException(AUTH_EXPIRED_TOKEN);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader(JWT_HEADER_KEY.getValue());
        if (authorization != null && authorization.startsWith(JWT_PREFIX.getValue())) {
            return authorization.split(" ")[1];
        }

        log.info("토큰이 없습니다.");
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // 화이트리스트 경로에 대해서는 JWT 필터 제외
        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/api-docs")
                || path.startsWith("/actuator/health")
                || path.startsWith("/oauth2/authorization")
                || path.startsWith("/login/oauth2/code")
                || path.startsWith("/auth/users")
                || path.equals("/auth/token")
                ;
    }

}
