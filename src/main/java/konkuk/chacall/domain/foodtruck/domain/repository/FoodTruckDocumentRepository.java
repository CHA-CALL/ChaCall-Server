package konkuk.chacall.domain.foodtruck.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruckDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface FoodTruckDocumentRepository extends JpaRepository<FoodTruckDocument, Long> {
    @Query("""
            SELECT ftd
            FROM FoodTruckDocument ftd
            WHERE ftd.foodTruck.foodTruckId IN :foodTruckIds
            """)
    List<FoodTruckDocument> findAllInFoodTruckIds(List<Long> foodTruckIds);
}
