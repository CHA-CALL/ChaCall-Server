package konkuk.chacall.domain.foodtruck.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FoodTruckRepository extends JpaRepository<FoodTruck, Long> {

    @Query("SELECT ft FROM FoodTruck ft " +
            "WHERE ft.owner.userId = :ownerId " +
            "AND ft.foodTruckId < :lastCursor " +
            "ORDER BY ft.foodTruckId DESC")
    Slice<FoodTruck> findByOwnerUserIdWithCursor(@Param("ownerId") Long ownerId, @Param("lastCursor") Long lastCursor, Pageable pageable);

}
