package konkuk.chacall.domain.foodtruck.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.infra.FoodTruckSearchRepository;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
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
                  and f.foodTruckStatus = :status
            """)
    Optional<FoodTruck> findByFoodTruckIdAndOwnerIdAndFoodTruckStatus(
            @Param("foodTruckId") Long foodTruckId,
            @Param("ownerId") Long ownerId,
            @Param("status") FoodTruckStatus status);

    @Query("""
                select (count(f) > 0)
                from FoodTruck f
                where f.foodTruckId = :foodTruckId
                  and f.owner.userId = :ownerId
                  and f.foodTruckStatus = :status
            """)
    boolean existsByFoodTruckIdAndOwnerIdAndFoodTruckStatus(
            @Param("foodTruckId") Long foodTruckId,
            @Param("ownerId") Long ownerId,
            @Param("status") FoodTruckStatus status
    );

    boolean existsByFoodTruckInfo_Name(String name);

    @EntityGraph(attributePaths = {"owner"})
    List<FoodTruck> findAllByFoodTruckStatus(FoodTruckStatus foodTruckStatus);

    @EntityGraph(attributePaths = {"owner"})
    List<FoodTruck> findAll();

    @Query("""
                select f
                from FoodTruck f
                where f.foodTruckStatus = 'APPROVED'
                and f.foodTruckViewedStatus = 'ON'
                order by f.ratingInfo.averageRating desc, f.ratingInfo.ratingCount desc
                limit :limit
            """)
    List<FoodTruck> findTopRatedFoodTrucks(@Param("limit") int limit);
}
