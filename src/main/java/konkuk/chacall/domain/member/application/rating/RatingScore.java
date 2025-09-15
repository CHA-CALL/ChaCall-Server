package konkuk.chacall.domain.member.application.rating;

import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RatingScore {

    RATING_0(0.0),
    RATING_0_5(0.5),
    RATING_1(1.0),
    RATING_1_5(1.5),
    RATING_2(2.0),
    RATING_2_5(2.5),
    RATING_3(3.0),
    RATING_3_5(3.5),
    RATING_4(4.0),
    RATING_4_5(4.5),
    RATING_5(5.0);

    private final double value;

    public static RatingScore fromValue(double value) {
        for (RatingScore score : values()) {
            if (Double.compare(score.value, value) == 0) {
                return score;
            }
        }
        throw new BusinessException(ErrorCode.RATING_INVALID_SCORE);
    }
}