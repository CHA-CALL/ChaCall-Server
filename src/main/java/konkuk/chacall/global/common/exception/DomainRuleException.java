package konkuk.chacall.global.common.exception;

import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class DomainRuleException extends BusinessException {
    public DomainRuleException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DomainRuleException(ErrorCode errorCode, Exception e) {
        super(errorCode, e);
    }
}
