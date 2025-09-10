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
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/ping")
    public BaseResponse<String> ping() {
        return BaseResponse.ok("pong");
    }

    // === 커스텀 예외들 ===
    @GetMapping("/auth-error")
    public String authError() {
        throw new AuthException(ErrorCode.AUTH_UNAUTHORIZED);
    }

    @GetMapping("/entity-error")
    public String entityError() {
        throw new EntityNotFoundException(ErrorCode.USER_NOT_FOUND);
    }

    @GetMapping("/domain-error")
    public String domainError() {
        throw new DomainRuleException(ErrorCode.USER_ALREADY_EXISTS);
    }

    @GetMapping("/business-error")
    public String businessError() {
        throw new BusinessException(ErrorCode.USER_NICKNAME_DUPLICATION);
    }

    @GetMapping("/runtime-error")
    public String runtimeError() {
        throw new RuntimeException("강제 RuntimeException 발생");
    }

    // === GlobalExceptionHandler 내장 케이스들 ===

    // 1. MethodArgumentNotValidException 테스트 (@Valid DTO 사용)
    @PostMapping("/validate-body")
    public String validateBody(@Valid @RequestBody UserRequest request) {
        return "유효성 통과: " + request.toString();
    }

    // 2. MethodArgumentTypeMismatchException 테스트
    // 호출: /test/type-mismatch?id=문자열
    @GetMapping("/type-mismatch")
    public String typeMismatch(@RequestParam("id") Long id) {
        return "입력한 id: " + id;
    }

    // 3. MissingServletRequestParameterException 테스트
    // 호출 시 /test/missing-param 만 요청 -> user 파라미터 없음
    @GetMapping("/missing-param")
    public String missingParam(@RequestParam("user") String user) {
        return "user: " + user;
    }

    // 4. ConstraintViolationException 테스트
    // 호출: /test/constraint?id=-5
    @GetMapping("/constraint")
    public String constraint(@RequestParam("id") @Valid @Min(1) int id) {
        return "id: " + id;
    }

    // DTO 내부 유효성 검증용 클래스
    @Getter
    private static class UserRequest {
        @NotBlank(message = "이름은 필수 값입니다.")
        private String name;

        @Size(min = 5, max = 20, message = "닉네임은 5~20자 사이여야 합니다.")
        private String nickname;

        @Override
        public String toString() {
            return "UserRequest{name='" + name + "', nickname='" + nickname + "'}";
        }
    }
}