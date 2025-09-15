package konkuk.chacall.domain.foodtruck.domain.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import konkuk.chacall.domain.member.domain.Rating;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RatingInfo {

    @Column(nullable = false)
    private double averageRating = 0.0;

    @Column(nullable = false)
    private int ratingCount = 0;

    public static RatingInfo createInitial() {
        return RatingInfo.builder()
                .averageRating(0.0)
                .ratingCount(0)
                .build();
    }

    // 새로운 평점이 추가될 때마다 평균 평점과 평점 수를 업데이트하는 메서드
    public void updateAverageRating(double newRating) {
        double totalRating = this.averageRating * this.ratingCount;
        totalRating += newRating;
        this.ratingCount++;
        this.averageRating = totalRating / this.ratingCount;
    }
}
