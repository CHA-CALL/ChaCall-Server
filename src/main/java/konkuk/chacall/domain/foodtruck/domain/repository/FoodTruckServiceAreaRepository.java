package konkuk.chacall.domain.foodtruck.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.FoodTruckServiceArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodTruckServiceAreaRepository extends JpaRepository<FoodTruckServiceArea, Long> {

    @Query("SELECT ftsa FROM FoodTruckServiceArea ftsa " +
            "JOIN FETCH ftsa.region r " +
            "WHERE ftsa.foodTruck.foodTruckId IN :foodTruckIds")
    List<FoodTruckServiceArea> findAllWithRegionByFoodTruckIdIn(@Param("foodTruckIds") List<Long> foodTruckIds);

}
