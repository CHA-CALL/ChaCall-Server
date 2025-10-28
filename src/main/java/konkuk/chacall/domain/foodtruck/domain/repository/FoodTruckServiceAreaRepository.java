package konkuk.chacall.domain.foodtruck.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruckServiceArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FoodTruckServiceAreaRepository extends JpaRepository<FoodTruckServiceArea, Long> {

    @Query("SELECT ftsa FROM FoodTruckServiceArea ftsa " +
            "JOIN FETCH ftsa.region r " +
            "WHERE ftsa.foodTruck.foodTruckId IN :foodTruckIds")
    List<FoodTruckServiceArea> findAllWithRegionByFoodTruckIdIn(@Param("foodTruckIds") List<Long> foodTruckIds);

    @Modifying
    @Query("DELETE FROM FoodTruckServiceArea ftsa WHERE ftsa.foodTruck.foodTruckId = :foodTruckId")
    void deleteAllByFoodTruckId(@Param("foodTruckId") Long foodTruckId);

    @Query("SELECT ftsa FROM FoodTruckServiceArea ftsa " +
            "WHERE ftsa.foodTruck.foodTruckId = :foodTruckId")
    List<FoodTruckServiceArea> findAllByFoodTruckId(Long foodTruckId);


    @Query("SELECT ftsa FROM FoodTruckServiceArea ftsa " +
            "WHERE ftsa.foodTruck.foodTruckId = :foodTruckId " +
            "AND ftsa.region.regionId = :regionId")
    Optional<FoodTruckServiceArea> findByFoodTruckIdAndRegionId(Long foodTruckId, Long regionId);
}
