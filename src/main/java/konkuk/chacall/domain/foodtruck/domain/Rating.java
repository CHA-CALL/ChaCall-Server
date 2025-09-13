package konkuk.chacall.domain.foodtruck.domain;

import jakarta.persistence.*;
import konkuk.chacall.domain.user.domain.User;
import konkuk.chacall.global.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ratings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Rating extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id", nullable = false)
    private Long ratingId;

    private Double rating;

    @Column(nullable = false)
    private boolean isRated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_truck_id", nullable = false)
    private FoodTruck foodTruck;
}
