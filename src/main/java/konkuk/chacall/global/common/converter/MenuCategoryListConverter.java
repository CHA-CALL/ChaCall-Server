package konkuk.chacall.global.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import konkuk.chacall.domain.foodtruck.domain.value.MenuCategory;
import konkuk.chacall.domain.foodtruck.domain.value.MenuCategoryList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter(autoApply = false)
public class MenuCategoryListConverter implements AttributeConverter<MenuCategoryList, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(MenuCategoryList attribute) {
        if (attribute == null || attribute.size() == 0) {
            return "";
        }
        return attribute.getMenuCategories().stream()
                .map(MenuCategory::getValue)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public MenuCategoryList convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return MenuCategoryList.of(List.of());
        }
        List<MenuCategory> categories = Arrays.stream(dbData.split(DELIMITER))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(MenuCategory::from)
                .toList();
        return MenuCategoryList.of(categories);
    }
}