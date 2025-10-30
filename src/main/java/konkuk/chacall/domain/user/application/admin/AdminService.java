package konkuk.chacall.domain.user.application.admin;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruckDocument;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckDocumentRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckStatus;
import konkuk.chacall.domain.user.presentation.dto.request.ApproveFoodTruckStatusRequest;
import konkuk.chacall.domain.user.presentation.dto.response.FoodTruckForAdminResponse;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final FoodTruckRepository foodTruckRepository;
    private final FoodTruckDocumentRepository foodTruckDocumentRepository;

    public void approveFoodTruckStatus(Long foodTruckId, ApproveFoodTruckStatusRequest request) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FOOD_TRUCK_NOT_FOUND));

        foodTruck.approveFoodTruck(request.status());
    }

    public List<FoodTruckForAdminResponse> getAllFoodTrucks(String status) {
        List<FoodTruck> foodTruckList;

        if(status != null && !status.isEmpty()) {
            FoodTruckStatus foodTruckStatus = FoodTruckStatus.from(status);
            foodTruckList = foodTruckRepository.findAllByFoodTruckStatus(foodTruckStatus);
        } else {
            foodTruckList = foodTruckRepository.findAll();
        }

        Map<Long, List<FoodTruckDocument>> documentMap = foodTruckDocumentRepository.findAllInFoodTruckIds(
                        foodTruckList.stream()
                                .map(FoodTruck::getFoodTruckId)
                                .toList()
                ).stream()
                .collect(
                        Collectors.groupingBy(document -> document.getFoodTruck().getFoodTruckId())
                );


        return foodTruckList.stream()
                .map(foodTruck -> FoodTruckForAdminResponse.from(
                            foodTruck,
                            documentMap.getOrDefault(foodTruck.getFoodTruckId(), List.of()).stream()
                                    .map(FoodTruckDocument::getDocumentUrl)
                                    .collect(Collectors.toList())
                    )
                )
                .collect(Collectors.toList());
    }
}
