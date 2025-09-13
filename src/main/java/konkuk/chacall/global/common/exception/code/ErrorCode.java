package konkuk.chacall.global.common.exception.code;

import konkuk.chacall.global.common.dto.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements ResponseCode {

    API_NOT_FOUND(HttpStatus.NOT_FOUND, 40400, "요청한 API를 찾을 수 없습니다."),
    API_METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 40500, "허용되지 않는 HTTP 메소드입니다."),
    API_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "서버 내부 오류입니다."),

    API_BAD_REQUEST(HttpStatus.BAD_REQUEST, 40000, "잘못된 요청입니다."),
    API_MISSING_PARAM(HttpStatus.BAD_REQUEST, 40001, "필수 파라미터가 없습니다."),
    API_INVALID_PARAM(HttpStatus.BAD_REQUEST, 40002, "파라미터 값 중 유효하지 않은 값이 있습니다."),
    API_INVALID_TYPE(HttpStatus.BAD_REQUEST, 40003, "파라미터 타입이 잘못되었습니다."),

    AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 40100, "유효하지 않은 토큰입니다."),
    AUTH_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, 40101, "만료된 토큰입니다."),
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 40102, "인증되지 않은 사용자입니다."),
    AUTH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, 40103, "토큰을 찾을 수 없습니다."),
    AUTH_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, 40104, "로그인에 실패했습니다."),
    AUTH_UNSUPPORTED_SOCIAL_LOGIN(HttpStatus.UNAUTHORIZED, 40105, "지원하지 않는 소셜 로그인입니다."),
    AUTH_INVALID_LOGIN_TOKEN_KEY(HttpStatus.UNAUTHORIZED, 40106, "유효하지 않은 로그인 토큰 키입니다."),

    /* 60000부터 비즈니스 예외 */
    /**
     * User
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 60001, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, 60002, "이미 존재하는 사용자입니다."),
    USER_NICKNAME_DUPLICATION(HttpStatus.CONFLICT, 60003, "이미 존재하는 닉네임입니다."),

    /**
     * BankAccount
     */
    BANK_ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, 70001, "계좌를 찾을 수 없습니다."),
    BANK_ACCOUNT_ALREADY_EXISTS(HttpStatus.CONFLICT, 70002, "이미 존재하는 계좌입니다."),
    BANK_ACCOUNT_ALREADY_EXISTS_FOR_USER(HttpStatus.CONFLICT, 70003, "해당 유저에게 이미 등록된 계좌가 존재합니다.")
    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
