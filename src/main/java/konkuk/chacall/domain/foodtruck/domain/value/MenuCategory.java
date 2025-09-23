package konkuk.chacall.domain.foodtruck.domain.value;

import konkuk.chacall.global.common.dto.EnumValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuCategory implements EnumValue {
    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    WESTERN("양식"),
    SNACK("분식"),
    CAFE_DESSERT("카페/디저트"),
    ETC("기타");

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
