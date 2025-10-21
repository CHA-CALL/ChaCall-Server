package konkuk.chacall.domain.foodtruck.domain.value;

import jakarta.persistence.*;
import konkuk.chacall.global.common.converter.MenuCategoryListConverter;
import konkuk.chacall.global.common.converter.PhotoUrlListConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Embeddable
public class FoodTruckInfo {

    @Column(nullable = false)
    private String name;

    @Column(length = 300)
    private String description;

    @Column(length = 15)
    private String phoneNumber;

    private String activeTime;

    private Boolean timeDiscussRequired;

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

    public static FoodTruckInfo createEmptyFoodTruckInfo(String name) {
        return FoodTruckInfo.builder()
                .name(name)
                .build();
    }
}
