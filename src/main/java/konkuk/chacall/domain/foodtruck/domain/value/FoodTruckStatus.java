package konkuk.chacall.domain.foodtruck.domain.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FoodTruckStatus {
    PENDING("승인 대기"),       // 아직 관리자의 승인이 끝나지 않은 상태
    ON("승인 완료"),
    REJECTED("승인 거부");     // 관리자가 승인을 거부한 상태

    private final String description;
}