package konkuk.chacall.domain.foodtruck.domain.model;

import jakarta.persistence.*;
import konkuk.chacall.domain.foodtruck.domain.value.*;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.domain.BaseEntity;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @Embedded
    private FoodTruckInfo foodTruckInfo;

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

    @Builder.Default
    @ColumnDefault("false")
    private Boolean canChangeViewedStatus = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    public static FoodTruck createEmptyFoodTruck(User owner, String name) {
        return FoodTruck.builder()
                .owner(owner)
                .foodTruckInfo(FoodTruckInfo.createEmptyFoodTruckInfo(name))
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

    public void validateApprovedStatus() {
        if (this.foodTruckStatus != FoodTruckStatus.APPROVED) {
            throw new DomainRuleException(ErrorCode.FOOD_TRUCK_NOT_APPROVED);
        }
    }

    // 푸드트럭 노출 상태 변경 허용 (최소 1번의 정보 기입 후)
    public void permitChangeViewStatus() {
        this.canChangeViewedStatus = true;
    }

    public void updateFoodTruckInfo(
            String name,
            String description,
            String phoneNumber,
            String activeTime,
            Boolean timeDiscussRequired,
            List<String> photoUrls,
            List<MenuCategory> menuCategories,
            AvailableQuantity availableQuantity,
            NeedElectricity needElectricity,
            PaymentMethod paymentMethod,
            String operatingInfo,
            String option
    ) {
        this.foodTruckInfo.updateFoodTruckInfo(
                name,
                description,
                phoneNumber,
                activeTime,
                timeDiscussRequired,
                PhotoUrlList.of(photoUrls),
                MenuCategoryList.of(menuCategories),
                availableQuantity,
                needElectricity,
                paymentMethod,
                operatingInfo,
                option
        );
    }

    public void updateAverageRating(double rating) {
        ratingInfo.updateAverageRating(rating);
    }

    public void approveFoodTruck(FoodTruckStatus targetFoodTruckStatus) {

        // 운영자 - 승인 대기 -> 승인 OR 승인 거부
        if (this.foodTruckStatus == FoodTruckStatus.PENDING && (targetFoodTruckStatus == FoodTruckStatus.APPROVED || targetFoodTruckStatus == FoodTruckStatus.REJECTED)) {
            this.foodTruckStatus = targetFoodTruckStatus;
            return;
        }

        // 운영자 - 승인 거부 -> 승인
        if (this.foodTruckStatus == FoodTruckStatus.REJECTED && targetFoodTruckStatus == FoodTruckStatus.APPROVED) {
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

    // 푸드트럭의 운영 기간을 반환해주는 메서드
    public List<String> getAvailableDates(List<AvailableDate> availableDateList) {
        return availableDateList.stream()
                .map(AvailableDate::formatDate)
                .collect(Collectors.toList());
    }

    public void changeViewedStatus(FoodTruckViewedStatus targetViewedStatus) {
        if(this.foodTruckViewedStatus == targetViewedStatus) {
            throw new DomainRuleException(ErrorCode.INVALID_FOOD_TRUCK_STATUS_TRANSITION);
        }

        if(!this.canChangeViewedStatus) {
            throw new DomainRuleException(ErrorCode.FOOD_TRUCK_VIEWED_STATUS_CHANGE_NOT_PERMITTED,
                    new IllegalArgumentException("최소 1번의 정보 기입 후에만 푸드트럭 노출 상태를 변경할 수 있습니다."));
        }

        this.foodTruckViewedStatus = targetViewedStatus;
    }

    public void validateViewableStatusForMember() {
        if (this.foodTruckViewedStatus != FoodTruckViewedStatus.ON) {
            throw new DomainRuleException(ErrorCode.FOOD_TRUCK_NOT_VIEWABLE);
        }
    }

    public void vaildateViewableStatusForOwner(Long userId) {
        if(!isOwnedBy(userId)) { // 자신이 소유한 푸드트럭이 아니면
            validateViewableStatusForMember();
        } else { // 자신이 소유한 푸드트럭이면
            validateOwner(userId);
        }
    }
}
