package konkuk.chacall.global.common.security.resolver;

import konkuk.chacall.global.common.annotation.UserId;
import konkuk.chacall.global.common.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import static konkuk.chacall.global.common.exception.code.ErrorCode.AUTH_TOKEN_NOT_FOUND;
import static konkuk.chacall.global.common.security.constant.AuthParameters.JWT_ACCESS_TOKEN_KEY;

@Component
@RequiredArgsConstructor
public class StompUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, Message<?> message) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || accessor.getSessionAttributes() == null) {
            throw new AuthException(AUTH_TOKEN_NOT_FOUND);
        }

        Object userId = accessor.getSessionAttributes()
                .get(JWT_ACCESS_TOKEN_KEY.getValue());

        if (userId == null) {
            throw new AuthException(AUTH_TOKEN_NOT_FOUND);
        }

        return (Long) userId;
    }
}