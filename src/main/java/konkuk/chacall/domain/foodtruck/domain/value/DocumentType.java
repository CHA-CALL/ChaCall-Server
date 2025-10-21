package konkuk.chacall.domain.foodtruck.domain.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentType {

    BUSINESS_LICENSE("사업자등록증"),
    OTHER("기타 서류");

    private final String value;
}
