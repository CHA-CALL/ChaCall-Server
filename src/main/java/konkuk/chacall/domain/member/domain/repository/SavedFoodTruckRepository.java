package konkuk.chacall.domain.member.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import konkuk.chacall.domain.member.domain.SavedFoodTruck;
import konkuk.chacall.domain.user.domain.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SavedFoodTruckRepository extends JpaRepository<SavedFoodTruck, Long> {

    boolean existsByMemberAndFoodTruck(User member, FoodTruck foodTruck);

    Optional<SavedFoodTruck> findByMemberAndFoodTruck(User member, FoodTruck foodTruck);

    @EntityGraph(attributePaths = {"foodTruck"})
    @Query("SELECT sft FROM SavedFoodTruck sft " +
            "WHERE sft.member = :member " +
            "AND sft.savedFoodTruckId < :lastCursor " +
            "ORDER BY sft.savedFoodTruckId DESC")
    Slice<SavedFoodTruck> findMemberSavedFoodTruckWithCursor(
            @Param("member") User member,
            @Param("lastCursor") Long lastCursor,
            Pageable pageable);

    @Modifying
    @Query("DELETE FROM SavedFoodTruck sft WHERE sft.foodTruck.foodTruckId = :foodTruckId")
    void deleteAllByFoodTruckId(@Param("foodTruckId") Long foodTruckId);
}
