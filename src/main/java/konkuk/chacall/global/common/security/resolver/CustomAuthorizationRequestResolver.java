package konkuk.chacall.global.common.security.resolver;

import jakarta.servlet.http.HttpServletRequest;
import konkuk.chacall.global.common.security.property.ServerWebProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static konkuk.chacall.global.common.security.constant.AuthParameters.*;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver delegate;
    private final ServerWebProperties serverWebProperties;

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository repo,
                                              String authorizationRequestBaseUri,
                                              ServerWebProperties props) {
        this.delegate = new DefaultOAuth2AuthorizationRequestResolver(repo, authorizationRequestBaseUri);
        this.serverWebProperties = props;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest base = delegate.resolve(request);
        return customize(request, base);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest base = delegate.resolve(request, clientRegistrationId);
        return customize(request, base);
    }

    private OAuth2AuthorizationRequest customize(HttpServletRequest request, OAuth2AuthorizationRequest base) {
        if (base == null) return null;

        String redirectUri = request.getParameter(REDIRECT_URI_KEY.getValue());
        Map<String, Object> additional = new HashMap<>(base.getAdditionalParameters());
        if (StringUtils.hasText(redirectUri) && serverWebProperties.isAllowed(redirectUri)) {
            request.getSession(true).setAttribute(REDIRECT_SESSION_KEY.getValue(), redirectUri);
        }

        return OAuth2AuthorizationRequest.from(base)
                .additionalParameters(additional)
                .build();
    }
}