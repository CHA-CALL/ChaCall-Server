package konkuk.chacall.domain.test;

import konkuk.chacall.global.common.dto.BaseResponse;
import konkuk.chacall.global.common.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@Profile("!prod")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final JwtUtil jwtUtil;

    @GetMapping("/token")
    public BaseResponse<String> getToken(@RequestParam Long userId) {
        String token = jwtUtil.createAccessToken(userId);
        return BaseResponse.ok(token);
    }
}