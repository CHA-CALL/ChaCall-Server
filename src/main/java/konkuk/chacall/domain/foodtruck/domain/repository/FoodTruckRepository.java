package konkuk.chacall.domain.foodtruck.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.infra.FoodTruckSearchRepository;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface FoodTruckRepository extends JpaRepository<FoodTruck, Long>, FoodTruckSearchRepository {

    @Query("SELECT ft FROM FoodTruck ft " +
            "WHERE ft.owner.userId = :ownerId " +
            "AND ft.foodTruckId < :lastCursor " +
            "ORDER BY ft.foodTruckId DESC")
    Slice<FoodTruck> findByOwnerUserIdWithCursor(@Param("ownerId") Long ownerId, @Param("lastCursor") Long lastCursor, Pageable pageable);

    @Query("""
                select f
                from FoodTruck f
                where f.foodTruckId = :foodTruckId
                  and f.owner.userId = :ownerId
                  and f.foodTruckStatus in :statuses
            """)
    Optional<FoodTruck> findByFoodTruckIdAndOwnerIdAndFoodTruckStatusIn(
            @Param("foodTruckId") Long foodTruckId,
            @Param("ownerId") Long ownerId,
            @Param("statuses") Collection<FoodTruckStatus> statuses);

    @Query("""
                select (count(f) > 0)
                from FoodTruck f
                where f.foodTruckId = :foodTruckId
                  and f.owner.userId = :ownerId
                  and f.foodTruckStatus in :statuses
            """)
    boolean existsByFoodTruckIdAndOwnerIdAndFoodTruckStatusIn(
            @Param("foodTruckId") Long foodTruckId,
            @Param("ownerId") Long ownerId,
            @Param("statuses") Collection<FoodTruckStatus> statuses
    );

    boolean existsByNameIgnoreCase(String name);

}
