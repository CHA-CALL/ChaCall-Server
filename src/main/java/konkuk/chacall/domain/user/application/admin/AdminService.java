package konkuk.chacall.domain.user.application.admin;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.user.presentation.dto.request.ApproveFoodTruckStatusRequest;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final FoodTruckRepository foodTruckRepository;

    public void approveFoodTruckStatus(Long foodTruckId, ApproveFoodTruckStatusRequest request) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FOOD_TRUCK_NOT_FOUND));

        foodTruck.approveFoodTruck(request.status());
    }

}
