package konkuk.chacall.domain.member.domain.repository;

import konkuk.chacall.domain.member.domain.SavedFoodTruck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SavedFoodTruckRepository extends JpaRepository<SavedFoodTruck, Long> {
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM SavedFoodTruck s " +
            "WHERE s.member.userId = :memberId AND s.foodTruck.foodTruckId = :foodTruckId")
    boolean existsByMemberIdAndFoodTruckId(Long memberId, Long foodTruckId);

    @Query("SELECT s FROM SavedFoodTruck s " +
            "WHERE s.member.userId = :userId AND s.foodTruck.foodTruckId = :foodTruckId")
    Optional<SavedFoodTruck> findByUserIdAndFoodTruckId(Long userId, Long foodTruckId);
}
