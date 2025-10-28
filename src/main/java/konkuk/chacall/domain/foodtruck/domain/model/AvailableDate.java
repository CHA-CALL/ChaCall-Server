package konkuk.chacall.domain.foodtruck.domain.model;

import jakarta.persistence.*;
import konkuk.chacall.global.common.domain.BaseEntity;
import lombok.*;

import java.time.LocalDate;

@Builder
@Entity
@Table(name = "available_dates")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AvailableDate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "available_date_id", nullable = false)
    private Long availableDateId;

    @Column(nullable = false)
    private LocalDate startAt;

    @Column(nullable = false)
    private LocalDate endAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_truck_id", nullable = false)
    private FoodTruck foodTruck;

    public static AvailableDate createAvailableDate(LocalDate startDate, LocalDate endDate, FoodTruck foodTruck) {
        return AvailableDate.builder()
                .startAt(startDate)
                .endAt(endDate)
                .foodTruck(foodTruck)
                .build();
    }
}
