package konkuk.chacall.domain.foodtruck.domain;

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

    @Builder.Default
    @Column(nullable = false)
    private RatingInfo ratingInfo = RatingInfo.createInitial();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

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

    // 푸드트럭의 호출 가능 지역을 반환해주는 메서드
    public String getServiceAreas(List<FoodTruckServiceArea> serviceAreaList) {
        return serviceAreaList.stream()
                .map(serviceArea -> serviceArea.getRegion().getFullName())
                .collect(Collectors.joining(", "));
    }
}
