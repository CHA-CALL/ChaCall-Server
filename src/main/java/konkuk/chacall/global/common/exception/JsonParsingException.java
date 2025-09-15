package konkuk.chacall.global.common.exception;

import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.Getter;

import static konkuk.chacall.global.common.exception.code.ErrorCode.*;

@Getter
public class JsonParsingException extends RuntimeException {
    private final ErrorCode errorCode = JSON_PARSING_ERROR;

    public JsonParsingException() {
    }
}
