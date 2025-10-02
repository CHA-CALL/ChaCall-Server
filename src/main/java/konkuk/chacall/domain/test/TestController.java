package konkuk.chacall.domain.test;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import konkuk.chacall.global.common.dto.BaseResponse;
import konkuk.chacall.global.common.exception.AuthException;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import konkuk.chacall.global.common.security.util.JwtUtil;
import lombok.Getter;
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