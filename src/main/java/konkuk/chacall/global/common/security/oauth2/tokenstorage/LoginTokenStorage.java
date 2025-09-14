package konkuk.chacall.global.common.security.oauth2.tokenstorage;

import konkuk.chacall.domain.user.domain.value.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

public interface LoginTokenStorage {

    void put(String key, Role role, String token, Duration ttl);

    /**
     * 저장된 토큰을 1회용으로 소비 후 삭제한다.
     * 존재하지 않으면 null 반환.
     */
    Entry consume(String key);

    record Entry(Role role, String token) {
    }
}