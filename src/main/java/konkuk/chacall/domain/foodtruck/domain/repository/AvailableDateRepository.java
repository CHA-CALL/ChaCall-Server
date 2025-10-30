package konkuk.chacall.domain.foodtruck.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.model.AvailableDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvailableDateRepository extends JpaRepository<AvailableDate, Long> {

    @Modifying
    @Query("DELETE FROM AvailableDate ad WHERE ad.foodTruck.foodTruckId = :foodTruckId")
    void deleteAllByFoodTruckId(@Param("foodTruckId") Long foodTruckId);

    @Query("SELECT ad FROM AvailableDate ad WHERE ad.foodTruck.foodTruckId = :foodTruckId")
    List<AvailableDate> findAllByFoodTruckId(Long foodTruckId);
}
