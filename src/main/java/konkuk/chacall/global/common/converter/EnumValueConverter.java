package konkuk.chacall.global.common.converter;

import konkuk.chacall.global.common.dto.EnumValue;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EnumValueConverter implements ConverterFactory<String, EnumValue> {

    @Override
    public <T extends EnumValue> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumValueConverter<>(targetType);
    }

    private static final class StringToEnumValueConverter<T extends EnumValue> implements Converter<String, T> {
        private final Class<T> targetType;

        private StringToEnumValueConverter(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String source) {

            String trimmedSource = source.trim();

            return Arrays.stream(targetType.getEnumConstants())
                    .filter(e -> e.getValue().equals(trimmedSource))
                    .findFirst()
                    .orElseThrow(() -> new DomainRuleException(ErrorCode.API_INVALID_PARAM));
        }
    }
}
