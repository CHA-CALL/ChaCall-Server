package konkuk.chacall.domain.foodtruck.application.duplicatecheck;

import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodTruckNameDuplicateCheckService {

    private final FoodTruckRepository foodTruckRepository;

    public boolean isNameDuplicated(String name) {
        return foodTruckRepository.existsByNameIgnoreCase(name);
    }
}
