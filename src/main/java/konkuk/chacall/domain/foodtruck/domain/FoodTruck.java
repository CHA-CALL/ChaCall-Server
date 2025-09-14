package konkuk.chacall.domain.foodtruck.domain;

import jakarta.persistence.*;
import konkuk.chacall.domain.foodtruck.domain.value.*;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.converter.MenuCategoryListConverter;
import konkuk.chacall.global.common.converter.PhotoUrlListConverter;
import konkuk.chacall.global.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "food_trucks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FoodTruck extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_truck_id", nullable = false)
    private Long foodTruckId;

    @Column(nullable = false)
    private String name;

    @Column(length = 300, nullable = false)
    private String description;

    @Column(length = 15, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String activeTime;

    @Column(nullable = false)
    private boolean timeDiscussRequired;

    @Convert(converter = PhotoUrlListConverter.class)
    @Column(nullable = false)
    private PhotoUrlList foodTruckPhotoList;

    @Convert(converter = MenuCategoryListConverter.class)
    @Column(nullable = false)
    private MenuCategoryList menuCategoryList;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AvailableQuantity availableQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NeedElectricity needElectricity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "operating_info", length = 800)
    private String operatingInfo;

    @Column(name = "option", length = 800)
    private String option;

    @Column(nullable = false)
    private double totalRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

}
