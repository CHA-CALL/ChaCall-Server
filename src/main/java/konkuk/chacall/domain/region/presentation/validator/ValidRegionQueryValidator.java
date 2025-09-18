package konkuk.chacall.domain.region.presentation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import konkuk.chacall.domain.region.domain.repository.RegionRepository;
import konkuk.chacall.domain.region.presentation.dto.request.RegionQueryRequest;

public class ValidRegionQueryValidator implements ConstraintValidator<ValidRegionQuery, RegionQueryRequest> {

    @Override
    public boolean isValid(RegionQueryRequest value, ConstraintValidatorContext ctx) {
        Integer depth = value.depth();
        Long parentCode = value.parentCode();

        // depth == 1 인데 parentCode 를 보냈으면 거절
        if (depth == 1 && parentCode != null) {
            addViolation(ctx, "depth = 1 인 경우 parentCode 를 보내면 안됩니다.", "parentCode");
            return false;
        }

        // depth >= 2 인데 parentCode 가 없으면 거절
        if (depth >= 2 && parentCode == null) {
            addViolation(ctx, "depth 가 2 또는 3일 때 parentCode 는 필수입니다.", "parentCode");
            return false;
        }

        return true;
    }

    private void addViolation(ConstraintValidatorContext ctx, String message, String property) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(property)
                .addConstraintViolation();
    }
}
