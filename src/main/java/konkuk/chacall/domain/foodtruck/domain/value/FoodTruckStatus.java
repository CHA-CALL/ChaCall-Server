package konkuk.chacall.domain.foodtruck.domain.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FoodTruckStatus {
    PENDING("승인 대기"),       // 아직 관리자의 승인이 끝나지 않은 상태
    APPROVED("승인 완료"),
    REJECTED("승인 거부");     // 관리자가 승인을 거부한 상태

    private final String description;

    public static FoodTruckStatus from(String status) {
        for (FoodTruckStatus foodTruckStatus : FoodTruckStatus.values()) {
            if (foodTruckStatus.getDescription().equals(status)) {
                return foodTruckStatus;
            }
        }
        throw new DomainRuleException(ErrorCode.FOOD_TRUCK_STATUS_MISMATCH);
    }
}