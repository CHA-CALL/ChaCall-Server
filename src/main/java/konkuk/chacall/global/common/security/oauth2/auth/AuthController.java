package konkuk.chacall.global.common.security.oauth2.auth;

import jakarta.validation.Valid;
import konkuk.chacall.domain.user.domain.repository.UserRepository;
import konkuk.chacall.global.common.dto.BaseResponse;
import konkuk.chacall.global.common.exception.AuthException;
import konkuk.chacall.global.common.security.oauth2.tokenstorage.LoginTokenStorage;
import konkuk.chacall.global.common.security.oauth2.auth.dto.AuthTokenRequest;
import konkuk.chacall.global.common.security.oauth2.auth.dto.AuthTokenResponse;
import konkuk.chacall.global.common.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static konkuk.chacall.global.common.exception.code.ErrorCode.API_INVALID_PARAM;
import static konkuk.chacall.global.common.exception.code.ErrorCode.AUTH_INVALID_LOGIN_TOKEN_KEY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final LoginTokenStorage loginTokenStorage;

    /**
     * loginTokenKey를 받아서, 해당 토큰이 유효한지 확인 후,
     * 유효하다면 JWT Access Token을 발급하여 응답으로 반환합니다.
     */
    @PostMapping("/token")
    public BaseResponse<AuthTokenResponse> getToken(
            @RequestBody @Valid AuthTokenRequest request
    ) {
        String loginTokenKey = request.loginTokenKey();

        LoginTokenStorage.Entry entry = loginTokenStorage.consume(loginTokenKey);
        if (entry == null) {
            throw new AuthException(AUTH_INVALID_LOGIN_TOKEN_KEY);
        }

        String token = entry.token();
        return BaseResponse.ok(AuthTokenResponse.of(token));
    }
}
