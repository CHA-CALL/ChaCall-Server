package konkuk.chacall.domain.foodtruck.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.model.AvailableDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AvailableDateRepository extends JpaRepository<AvailableDate, Long> {

    @Modifying
    @Query("DELETE FROM AvailableDate ad WHERE ad.foodTruck.foodTruckId = :foodTruckId")
    void deleteAllByFoodTruckId(@Param("foodTruckId") Long foodTruckId);
}
