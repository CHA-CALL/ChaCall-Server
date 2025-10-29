package konkuk.chacall.domain.member.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckViewedStatus;
import konkuk.chacall.domain.member.domain.SavedFoodTruck;
import konkuk.chacall.domain.user.domain.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SavedFoodTruckRepository extends JpaRepository<SavedFoodTruck, Long> {

    boolean existsByMemberAndFoodTruck(User member, FoodTruck foodTruck);

    Optional<SavedFoodTruck> findByMemberAndFoodTruck(User member, FoodTruck foodTruck);

    @EntityGraph(attributePaths = {"foodTruck"})
    @Query("SELECT sft FROM SavedFoodTruck sft " +
            "JOIN sft.foodTruck ft " +
            "WHERE sft.member = :member " +
            "AND ft.foodTruckViewedStatus = :status " +
            "AND sft.savedFoodTruckId < :lastCursor " +
            "ORDER BY sft.savedFoodTruckId DESC")
    Slice<SavedFoodTruck> findMemberSavedFoodTruckWithCursor(
            @Param("member") User member,
            @Param("status") FoodTruckViewedStatus status,
            @Param("lastCursor") Long lastCursor,
            Pageable pageable);

    @Modifying
    @Query("DELETE FROM SavedFoodTruck sft WHERE sft.foodTruck.foodTruckId = :foodTruckId")
    void deleteAllByFoodTruckId(@Param("foodTruckId") Long foodTruckId);

    @Query("""
                select s.foodTruck.foodTruckId
                  from SavedFoodTruck s
                 where s.member.userId = :userId
                   and s.foodTruck.foodTruckId in :foodTruckIds
            """)
    Set<Long> findSavedTruckIdsIn(@Param("userId") Long userId,
                                  @Param("foodTruckIds") List<Long> foodTruckIds);

    @Query("""
                select exists (
                    select s
                      from SavedFoodTruck s
                     where s.member.userId = :memberId
                       and s.foodTruck.foodTruckId = :foodTruckId
                )
            """)
    boolean existsByMemberIdAndFoodTruckId(Long userId, Long foodTruckId);
}
