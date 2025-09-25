package konkuk.chacall.domain.foodtruck.domain.model;

import jakarta.persistence.*;
import konkuk.chacall.domain.foodtruck.domain.value.MenuStatus;
import konkuk.chacall.global.common.domain.BaseEntity;
import lombok.*;

@Entity
@Table(name = "menus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(nullable = false, length = 18)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(length = 50)
    private String description;

    @Column(length = 2083)
    private String imageUrl;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MenuStatus menuStatus = MenuStatus.ON;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_truck_id", nullable = false)
    private FoodTruck foodTruck;

    public static Menu create(String name, Integer price, String description,
                              String imageUrl, FoodTruck foodTruck) {
        return Menu.builder()
                .name(name)
                .price(price)
                .description(description)
                .imageUrl(imageUrl)
                .menuStatus(MenuStatus.ON)
                .foodTruck(foodTruck)
                .build();
    }

    public String parsingMenuPrice() {
        return price + "원";
    }
}
