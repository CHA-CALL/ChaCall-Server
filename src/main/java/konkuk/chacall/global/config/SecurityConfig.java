package konkuk.chacall.global.config;

import konkuk.chacall.global.common.security.filter.JwtAuthenticationEntryPoint;
import konkuk.chacall.global.common.security.filter.JwtAuthenticationFilter;
import konkuk.chacall.global.common.security.oauth2.CustomOAuth2UserService;
import konkuk.chacall.global.common.security.oauth2.CustomSuccessHandler;
import konkuk.chacall.global.common.security.property.ServerWebProperties;
import konkuk.chacall.global.common.security.resolver.CustomAuthorizationRequestResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

import static konkuk.chacall.global.common.security.constant.AuthParameters.JWT_HEADER_KEY;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final ServerWebProperties serverWebProperties;

    private static final String[] WHITELIST = {
            "/swagger-ui/**", "/api-docs/**", "/swagger-ui.html",
            "/v3/api-docs/**","/oauth2/authorization/**",
            "/login/oauth2/code/**", "/actuator/health",
            "/auth/users", "/auth/token",

            "/index.html", "/static/**" // for test
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // redirect_uri(origin) → additionalParameters.return_to 저장
        var resolver = new CustomAuthorizationRequestResolver(
                clientRegistrationRepository,
                "/oauth2/authorization",
                serverWebProperties
        );

        // 세션 저장소
        var authReqRepo = new HttpSessionOAuth2AuthorizationRequestRepository();

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login((oauth2) -> oauth2
                        .authorizationEndpoint(ep -> ep
                                .authorizationRequestResolver(resolver)
                                .authorizationRequestRepository(authReqRepo)
                        )
                        .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(handler -> handler.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // CORS: server.web-domain-urls 사용
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(serverWebProperties.getWebDomainUrls());
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        config.setExposedHeaders(Collections.singletonList(JWT_HEADER_KEY.getValue()));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}