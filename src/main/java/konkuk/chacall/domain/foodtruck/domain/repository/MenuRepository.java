package konkuk.chacall.domain.foodtruck.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Modifying
    @Query("DELETE FROM Menu m WHERE m.foodTruck.foodTruckId = :foodTruckId")
    void deleteAllByFoodTruckId(@Param("foodTruckId") Long foodTruckId);
}
