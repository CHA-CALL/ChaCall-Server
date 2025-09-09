package konkuk.chacall.global.common.exception;

import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    private final ErrorCode errorCode;

    public AuthException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public AuthException(ErrorCode errorCode, Exception e) {
        super(e);
        this.errorCode = errorCode;
    }
}
