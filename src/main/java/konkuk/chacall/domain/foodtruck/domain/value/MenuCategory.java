package konkuk.chacall.domain.foodtruck.domain.value;

import konkuk.chacall.global.common.dto.EnumValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuCategory implements EnumValue {
    MEAL("식사"),
    LUNCHBOX("도시락"),
    FUSION("퓨전식"),
    SNACK("분식"),
    WESTERN("양식"),
    CHINESE("중식"),
    KOREAN("한식"),
    LIGHT_MEAL("간식"),
    DESSERT("디저트"),
    BEVERAGE("음료"),
    COFFEE("커피"),
    UNDECIDED("미정");

    private final String value;

    public static MenuCategory from(String value) {
        for (MenuCategory category : MenuCategory.values()) {
            if (category.getValue().equals(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown MenuCategory value: " + value);
    }
}
