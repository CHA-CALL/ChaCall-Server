package konkuk.chacall.domain.member.domain;

import jakarta.persistence.*;
import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.domain.BaseEntity;
import lombok.*;

@Builder
@Getter
@Entity
@Table(name = "saved_food_trucks")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavedFoodTruck extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saved_food_truck_id", nullable = false)
    private Long savedFoodTruckId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_truck_id", nullable = false)
    private FoodTruck foodTruck;

    public static SavedFoodTruck of(User user, FoodTruck foodTruck) {
        return SavedFoodTruck.builder()
                .member(user)
                .foodTruck(foodTruck)
                .build();
    }
}
