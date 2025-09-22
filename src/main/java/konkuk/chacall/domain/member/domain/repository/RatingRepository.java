package konkuk.chacall.domain.member.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.member.domain.Rating;
import konkuk.chacall.domain.user.domain.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByMemberAndFoodTruckAndIsRatedFalse(User member, FoodTruck foodTruck);

    @EntityGraph(attributePaths = {"foodTruck", "reservation"})
    @Query("SELECT r FROM Rating r " +
            "WHERE r.member = :member " +
            "AND r.isRated = false")
    List<Rating> findAllByMemberAndIsRatedFalse(User member);

    @Modifying
    @Query("DELETE FROM Rating r WHERE r.foodTruck.foodTruckId = :foodTruckId")
    void deleteAllByFoodTruckId(@Param("foodTruckId") Long foodTruckId);
}
