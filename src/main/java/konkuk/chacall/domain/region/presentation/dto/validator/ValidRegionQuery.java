package konkuk.chacall.domain.region.presentation.dto.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidRegionQueryValidator.class)
public @interface ValidRegionQuery {

    String message() default "잘못된 지역 조회 요청입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
