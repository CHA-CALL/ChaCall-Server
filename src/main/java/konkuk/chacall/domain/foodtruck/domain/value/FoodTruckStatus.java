package konkuk.chacall.domain.foodtruck.domain.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FoodTruckStatus {
    PENDING("승인 대기"),       // 아직 관리자의 승인이 끝나지 않은 상태
    ON("승인 완료 - 노출"),     // 승인 완료 + 사용자에게 보여지는 상태
    OFF("승인 완료 - 비노출");  // 승인 완료 + 사용자에게 보이지 않는 상태

    private final String description;
}