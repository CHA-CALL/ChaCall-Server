package konkuk.chacall.domain.member.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import konkuk.chacall.domain.member.domain.Rating;
import konkuk.chacall.domain.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByMemberAndFoodTruckAndIsRatedFalse(User member, FoodTruck foodTruck);

    @Query("SELECT r FROM Rating r " +
            "JOIN FETCH r.foodTruck f " +
            "JOIN FETCH r.reservation re " +
            "WHERE r.member = :member " +
            "AND r.isRated = false")
    List<Rating> findAllByMemberAndIsRatedFalse(User member);
}
