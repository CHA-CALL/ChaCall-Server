package konkuk.chacall.global.common.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import konkuk.chacall.global.common.dto.ErrorResponse;
import konkuk.chacall.global.common.exception.*;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static konkuk.chacall.global.common.exception.code.ErrorCode.*;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    // 요청한 API가 없는 경우
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> noHandlerExceptionHandler(NoHandlerFoundException e) {
        return ResponseEntity
                .status(API_NOT_FOUND.getHttpStatus())
                .body(ErrorResponse.of(API_NOT_FOUND));
    }

    // 허용되지 않은 HTTP 메소드로 요청한 경우
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error("[HttpRequestMethodNotSupportedExceptionHandler] {}", e.getMessage());
        return ResponseEntity
                .status(API_METHOD_NOT_ALLOWED.getHttpStatus())
                .body(ErrorResponse.of(API_METHOD_NOT_ALLOWED));
    }

    // 요청 파라미터가 유효하지 않은 경우
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidExceptionHandler] {}", e.getMessage());
        // 첫 번째 유효성 검사 실패 메시지만 가져오기
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Validation failed");

        return ResponseEntity
                .status(API_INVALID_PARAM.getHttpStatus())
                .body(ErrorResponse.of(API_INVALID_PARAM, errorMessage));
    }

    // 요청 파라미터의 타입이 맞지 않는 경우
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {
        log.error("[MethodArgumentTypeMismatchExceptionHandler] {}", e.getMessage());

        return ResponseEntity
                .status(API_INVALID_TYPE.getHttpStatus())
                .body(ErrorResponse.of(API_INVALID_TYPE, e.getName() + "는 " + e.getRequiredType() + " 타입이어야 합니다."));
    }

    // 요청 파라미터가 누락된 경우
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        log.error("[MissingServletRequestParameterExceptionHandler] {}", e.getMessage());
        return ResponseEntity
                .status(API_MISSING_PARAM.getHttpStatus())
                .body(ErrorResponse.of(API_MISSING_PARAM, e.getParameterName() + "를 추가해서 요청해주세요."));
    }

    // @Validation 예외처리
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error("[ConstraintViolationExceptionHandler] {}", e.getMessage());
        // 첫 번째 위반만 꺼내서
        ConstraintViolation<?> violation = e.getConstraintViolations().stream().findFirst().orElse(null);

        // 기본 메시지 또는 제약조건 메시지 사용
        String errorMessage = Optional.ofNullable(violation)
                .map(v -> v.getMessage())
                .orElse("유효성 검사에 실패했습니다.");

        // API_INVALID_PARAM 코드를 공통으로 사용
        return ResponseEntity
                .status(API_INVALID_PARAM.getHttpStatus())
                .body(ErrorResponse.of(API_INVALID_PARAM, errorMessage));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handlerMethodValidationException(HandlerMethodValidationException e) {
        log.error("[HandlerMethodValidationException] {}", e.getMessage());

        // 파라미터별 검증 실패 메시지를 모아서 detail 로 제공
        String detail = e.getParameterValidationResults().stream()
                .map(result -> {
                    String paramName = Optional.ofNullable(result.getMethodParameter().getParameterName())
                            .orElse("parameter");
                    String messages = result.getResolvableErrors().stream()
                            .map(MessageSourceResolvable::getDefaultMessage)
                            .filter(Objects::nonNull)
                            .collect(Collectors.joining(", "));
                    return paramName + ": " + (messages.isBlank() ? "유효하지 않은 값입니다." : messages);
                })
                .collect(Collectors.joining(" | "));

        if (detail.isBlank()) {
            detail = "유효성 검사에 실패했습니다.";
        }

        return ResponseEntity
                .status(API_INVALID_PARAM.getHttpStatus())
                .body(ErrorResponse.of(API_INVALID_PARAM, detail));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("[httpMessageNotReadableException] {}", e.getMessage());

        Throwable cause = e.getMostSpecificCause();
        if (cause instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            return ResponseEntity
                    .status(ErrorCode.INVALID_ENUM_VALUE.getHttpStatus())
                    .body(ErrorResponse.of(ErrorCode.INVALID_ENUM_VALUE,
                            "잘못된 값: " + ife.getValue()));
        }
        return ResponseEntity
                .status(ErrorCode.API_INVALID_PARAM.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.API_INVALID_PARAM, "잘못된 요청 본문입니다."));
    }

    // === 우선순위 1: 인증/인가 예외 ===
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> authExceptionHandler(AuthException e) {
        log.warn("[AuthException] {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    // === 우선순위 2: 엔티티 NotFound ===
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFoundExceptionHandler(EntityNotFoundException e) {
        log.info("[EntityNotFound] {}", e.getMessage());
        String detail = Optional.ofNullable(e.getCause()).map(Throwable::getMessage).orElse("");
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ErrorResponse.of(e.getErrorCode(), detail));
    }

    // === 우선순위 3: 도메인 규칙 위반 ===
    @ExceptionHandler(DomainRuleException.class)
    public ResponseEntity<ErrorResponse> domainRuleViolationExceptionHandler(DomainRuleException e) {
        log.warn("[DomainRuleViolation] {}", e.getMessage());
        String detail = Optional.ofNullable(e.getCause()).map(Throwable::getMessage).orElse("");
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ErrorResponse.of(e.getErrorCode(), detail));
    }

    // === 우선순위 4: 일반 비즈니스 예외(캐치올 for BusinessException) ===
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> businessExceptionHandler(BusinessException e) {
        log.warn("[BusinessException] {}", e.getMessage());
        String detail = Optional.ofNullable(e.getCause()).map(Throwable::getMessage).orElse("");
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ErrorResponse.of(e.getErrorCode(), detail));
    }

    // JSON 파싱 예외 처리
    @ExceptionHandler(JsonParsingException.class)
    public ResponseEntity<ErrorResponse> jsonParsingExceptionHandler(JsonParsingException e) {
        ErrorCode jsonErrorCode = e.getErrorCode();
        log.error("[JsonParsingException] {}", jsonErrorCode.getMessage());
        return ResponseEntity
                .status(jsonErrorCode.getHttpStatus())
                .body(ErrorResponse.of(jsonErrorCode));
    }

    // 서버 내부 오류 예외 처리
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeExceptionHandler(RuntimeException e) {
        log.error("[RuntimeExceptionHandler] {}", e.getMessage(), e);
        return ResponseEntity
                .status(API_SERVER_ERROR.getHttpStatus())
                .body(ErrorResponse.of(API_SERVER_ERROR));
    }

    // IllegalStateException 예외 처리
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> illegalStateExceptionHandler(IllegalStateException e) {
        log.error("[IllegalStateExceptionHandler] {}", e.getMessage());
        return ResponseEntity
                .status(API_SERVER_ERROR.getHttpStatus())
                .body(ErrorResponse.of(API_SERVER_ERROR));
    }

}
