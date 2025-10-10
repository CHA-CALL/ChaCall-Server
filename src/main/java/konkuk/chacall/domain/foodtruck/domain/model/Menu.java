package konkuk.chacall.domain.foodtruck.domain.model;

import jakarta.persistence.*;
import konkuk.chacall.domain.foodtruck.domain.value.MenuViewedStatus;
import konkuk.chacall.global.common.domain.BaseEntity;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
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
    private MenuViewedStatus menuViewedStatus = MenuViewedStatus.ON;

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
                .menuViewedStatus(MenuViewedStatus.ON)
                .foodTruck(foodTruck)
                .build();
    }

    public void updateMenu(String name, Integer price, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String parsingMenuPrice() {
        return price + "원";
    }

    public void changeViewedStatus(MenuViewedStatus targetViewedStatus) {
        if(this.menuViewedStatus == targetViewedStatus) {
            throw new DomainRuleException(ErrorCode.INVALID_MENU_STATUS_TRANSITION);
        }

        this.menuViewedStatus = targetViewedStatus;
    }
}
