package konkuk.chacall.global.common.storage.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CdnUrlResolver {

    @Value("${cloud.aws.s3.cloud-front-url}")
    private String cdnBaseUrl;

    public String resolve(String objectKey) {
        // CDN 도메인 기준으로 URL 구성
        // 예) https://cdn.example.com/reservations/1/estimate-1234.pdf
        String normalized = objectKey.startsWith("/") ? objectKey.substring(1) : objectKey;
        return String.format("%s/%s", trimRightSlash(cdnBaseUrl), normalized);
    }

    public String extractKeyFromUrl(String url) {
        if (url == null) return null;
        String base = trimRightSlash(cdnBaseUrl) + "/";
        if (url.startsWith(base)) {
            return url.substring(base.length());
        }
        // 다른 형태면 그대로 키 파싱 불가 → null
        return null;
    }

    private String trimRightSlash(String s) {
        if (s == null) return "";
        while (s.endsWith("/")) s = s.substring(0, s.length() - 1);
        return s;
    }
}