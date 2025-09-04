package konkuk.chacall.global.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import konkuk.chacall.domain.foodtruck.domain.value.PhotoUrlList;

import java.util.Arrays;
import java.util.List;

@Converter(autoApply = false)
public class PhotoUrlListConverter implements AttributeConverter<PhotoUrlList, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(PhotoUrlList attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return String.join(DELIMITER, attribute.getUrls());
    }

    @Override
    public PhotoUrlList convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return PhotoUrlList.of(List.of());
        }
        List<String> urls = Arrays.stream(dbData.split(DELIMITER))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        return PhotoUrlList.of(urls);
    }
}