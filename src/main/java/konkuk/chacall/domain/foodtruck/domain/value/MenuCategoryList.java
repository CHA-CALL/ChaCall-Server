package konkuk.chacall.domain.foodtruck.domain.value;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuCategoryList {

    private final List<MenuCategory> categories;

    public static MenuCategoryList of(List<MenuCategory> categories) {
        return new MenuCategoryList(categories == null ? List.of() : List.copyOf(categories));
    }

    public List<MenuCategory> getMenuCategories() {
        return Collections.unmodifiableList(categories);
    }

    public boolean contains(MenuCategory category) {
        return categories != null && categories.contains(category);
    }

    public int size() {
        return categories == null ? 0 : categories.size();
    }
}