package konkuk.chacall.domain.region.presentation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import konkuk.chacall.domain.region.domain.repository.RegionRepository;
import konkuk.chacall.domain.region.presentation.dto.request.RegionQueryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ValidRegionQueryValidator implements ConstraintValidator<ValidRegionQuery, RegionQueryRequest> {

    private final RegionRepository regionRepository;

    @Override
    public boolean isValid(RegionQueryRequest value, ConstraintValidatorContext ctx) {
        Integer depth = value.depth();
        Long parentCode = value.parentCode();

        // depth == 1 인데 parentCode 를 함께 보냈으면 거절 (요청부터 잘못된 값)
        if (depth == 1) {
            if (parentCode != null) {
                addViolation(ctx, "depth = 1 인 경우 parentCode 를 보내면 안됩니다.", "parentCode");
                return false;
            }
            return true;
        }

        // depth >= 2 -> parentCode 필수
        if (depth >= 2) {
            if (parentCode == null) {
                addViolation(ctx, "depth 가 2 또는 3일 때 parentCode 는 필수입니다.", "parentCode");
                return false;
            }

            if (!regionRepository.existsByRegionCode(parentCode)) {
                addViolation(ctx, "상위 지역이 존재하지 않습니다.", "parentCode");
                return false;
            }
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
