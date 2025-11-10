package konkuk.chacall.global.common.security.interceptor;

import konkuk.chacall.global.common.exception.AuthException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import konkuk.chacall.global.common.security.oauth2.LoginUser;
import konkuk.chacall.global.common.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import static konkuk.chacall.global.common.security.constant.AuthParameters.*;

@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // CONNECT 요청에 대해서 인증 처리 & JWT 토큰 검증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorization = accessor.getFirstNativeHeader(JWT_HEADER_KEY.getValue());
            if (authorization == null || !authorization.startsWith(JWT_PREFIX.getValue())) {
                throw new AuthException(ErrorCode.AUTH_TOKEN_NOT_FOUND);
            }

            String token = authorization.split(" ")[1];
            LoginUser loginUser = jwtUtil.getLoginUser(token);

            // ArgumentsResolver를 위해 세션에 userId 저장
            accessor.getSessionAttributes()
                    .put(JWT_ACCESS_TOKEN_KEY.getValue(), loginUser.userId());
        }

        return message;
    }
}
