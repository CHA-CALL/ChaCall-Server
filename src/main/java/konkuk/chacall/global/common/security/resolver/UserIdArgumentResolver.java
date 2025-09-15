package konkuk.chacall.global.common.security.resolver;

import jakarta.servlet.http.HttpServletRequest;
import konkuk.chacall.global.common.exception.AuthException;
import konkuk.chacall.global.common.security.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static konkuk.chacall.global.common.exception.code.ErrorCode.AUTH_TOKEN_NOT_FOUND;
import static konkuk.chacall.global.common.security.constant.AuthParameters.JWT_ACCESS_TOKEN_KEY;


@Component
@RequiredArgsConstructor
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Long resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        Object userId = ((HttpServletRequest) webRequest.getNativeRequest()).getAttribute(JWT_ACCESS_TOKEN_KEY.getValue());
        if (userId == null) {
            throw new AuthException(AUTH_TOKEN_NOT_FOUND);
        }
        return (Long) userId;
    }
}
