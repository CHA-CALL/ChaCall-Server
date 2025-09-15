package konkuk.chacall.domain.member.domain.repository;

import jakarta.validation.constraints.NotNull;
import konkuk.chacall.domain.member.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT r FROM Rating r " +
            "WHERE r.member.userId = :memberId AND r.foodTruck.foodTruckId = :foodTruckId AND r.isRated = false")
    Optional<Rating> findByMemberIdAndFoodTruckIdAndIsRatedFalse(Long memberId, Long foodTruckId);
}
