package konkuk.chacall.global.common.exception;


import konkuk.chacall.global.common.exception.code.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
