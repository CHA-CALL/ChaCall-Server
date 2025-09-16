package konkuk.chacall.domain.member.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import konkuk.chacall.domain.member.domain.SavedFoodTruck;
import konkuk.chacall.domain.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SavedFoodTruckRepository extends JpaRepository<SavedFoodTruck, Long> {

    boolean existsByMemberAndFoodTruck(User member, FoodTruck foodTruck);

    Optional<SavedFoodTruck> findByMemberAndFoodTruck(User member, FoodTruck foodTruck);
}
