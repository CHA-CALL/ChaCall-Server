package konkuk.chacall.domain.foodtruck.domain;

import jakarta.persistence.*;
import konkuk.chacall.domain.region.domain.Region;
import konkuk.chacall.global.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "food_truck_service_areas")
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
}
