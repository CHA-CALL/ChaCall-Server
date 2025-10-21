package konkuk.chacall.domain.foodtruck.domain.model;

import jakarta.persistence.*;
import konkuk.chacall.domain.foodtruck.domain.value.*;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.converter.MenuCategoryListConverter;
import konkuk.chacall.global.common.converter.PhotoUrlListConverter;
import konkuk.chacall.global.common.domain.BaseEntity;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Entity
@Table(name = "food_trucks")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FoodTruck extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_truck_id", nullable = false)
    private Long foodTruckId;

    @Column(nullable = false)
    private String name;

    @Column(length = 300)
    private String description;

    @Column(length = 15)
    private String phoneNumber;

    private String activeTime;

    private boolean timeDiscussRequired;

    @Convert(converter = PhotoUrlListConverter.class)
    private PhotoUrlList foodTruckPhotoList;

    @Convert(converter = MenuCategoryListConverter.class)
    private MenuCategoryList menuCategoryList;

    @Enumerated(EnumType.STRING)
    private AvailableQuantity availableQuantity;

    @Enumerated(EnumType.STRING)
    private NeedElectricity needElectricity;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "operating_info", length = 800)
    private String operatingInfo;

    @Column(name = "option", length = 800)
    private String option;

    @Column(name = "rejection_reason", length = 30)
    private String rejectionReason;

    @Builder.Default
    @Column(nullable = false)
    private RatingInfo ratingInfo = RatingInfo.createInitial();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FoodTruckStatus foodTruckStatus = FoodTruckStatus.PENDING;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private FoodTruckViewedStatus foodTruckViewedStatus = FoodTruckViewedStatus.OFF;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    public static FoodTruck createEmptyFoodTruck(User owner, String name) {
        return FoodTruck.builder()
                .owner(owner)
                .name(name)
                .description(null)
                .phoneNumber(null)
                .activeTime(null)
                .timeDiscussRequired(false)
                .foodTruckPhotoList(null)
                .menuCategoryList(null)
                .availableQuantity(null)
                .needElectricity(null)
                .paymentMethod(null)
                .operatingInfo(null)
                .option(null)
                .rejectionReason(null)
                .foodTruckStatus(FoodTruckStatus.PENDING)
                .foodTruckViewedStatus(FoodTruckViewedStatus.OFF)
                .ratingInfo(RatingInfo.createInitial())
                .build();
    }

    private boolean isOwnedBy(Long ownerId) {
        return this.owner.getUserId().equals(ownerId);
    }

    public void validateOwner(Long ownerId) {
        if (!isOwnedBy(ownerId)) {
            throw new DomainRuleException(ErrorCode.FOOD_TRUCK_NOT_OWNED);
        }
    }

    public void updateAverageRating(double rating) {
        ratingInfo.updateAverageRating(rating);
    }

    public void approveFoodTruck(FoodTruckStatus targetFoodTruckStatus) {

        // 운영자 - 승인 대기 -> 승인 OR 승인 거부
        if (this.foodTruckStatus == FoodTruckStatus.PENDING && (targetFoodTruckStatus == FoodTruckStatus.ON || targetFoodTruckStatus == FoodTruckStatus.REJECTED)) {
            this.foodTruckStatus = targetFoodTruckStatus;
            return;
        }

        // 운영자 - 승인 거부 -> 승인
        if (this.foodTruckStatus == FoodTruckStatus.REJECTED && targetFoodTruckStatus == FoodTruckStatus.ON) {
            this.foodTruckStatus = targetFoodTruckStatus;
            return;
        }

        throw new DomainRuleException(ErrorCode.INVALID_FOOD_TRUCK_STATUS_TRANSITION);
    }

    // 푸드트럭의 호출 가능 지역을 반환해주는 메서드
    public String getServiceAreas(List<FoodTruckServiceArea> serviceAreaList) {
        return serviceAreaList.stream()
                .map(serviceArea -> serviceArea.getRegion().getFullName())
                .collect(Collectors.joining(", "));
    }

    public void changeViewedStatus(FoodTruckViewedStatus targetViewedStatus) {
        if(this.foodTruckViewedStatus == targetViewedStatus) {
            throw new DomainRuleException(ErrorCode.INVALID_FOOD_TRUCK_STATUS_TRANSITION);
        }

        this.foodTruckViewedStatus = targetViewedStatus;
    }
}
