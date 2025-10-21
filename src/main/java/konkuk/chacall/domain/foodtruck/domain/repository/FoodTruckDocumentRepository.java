package konkuk.chacall.domain.foodtruck.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruckDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodTruckDocumentRepository extends JpaRepository<FoodTruckDocument, Long> {
}
