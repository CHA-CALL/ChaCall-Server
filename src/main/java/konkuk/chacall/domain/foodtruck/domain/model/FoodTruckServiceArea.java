package konkuk.chacall.domain.foodtruck.domain.model;

import jakarta.persistence.*;
import konkuk.chacall.domain.region.domain.model.Region;
import konkuk.chacall.global.common.domain.BaseEntity;
import lombok.*;

@Builder
@Entity
@Table(name = "food_truck_service_areas")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FoodTruckServiceArea extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_truck_service_area_id", nullable = false)
    private Long foodTruckServiceAreaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_truck_id", nullable = false)
    private FoodTruck foodTruck;

    public static FoodTruckServiceArea createFoodTruckServiceArea(Region region, FoodTruck foodTruck) {
        return FoodTruckServiceArea.builder()
                .region(region)
                .foodTruck(foodTruck)
                .build();
    }
}
