package konkuk.chacall.domain.member.application.foodtruck;

import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.member.domain.SavedFoodTruck;
import konkuk.chacall.domain.member.domain.repository.SavedFoodTruckRepository;
import konkuk.chacall.domain.member.presentation.dto.request.UpdateFoodTruckSaveStatusRequest;
import konkuk.chacall.domain.member.presentation.dto.response.SavedFoodTruckResponse;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.domain.user.domain.repository.UserRepository;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SavedFoodTruckService {

    private final SavedFoodTruckRepository savedFoodTruckRepository;
    private final UserRepository userRepository;
    private final FoodTruckRepository foodTruckRepository;

    @Transactional
    public SavedFoodTruckResponse updateFoodTruckSaveStatus(UpdateFoodTruckSaveStatusRequest request, Long foodTruckId, Long userId) {

        // 유저 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        // 푸드트럭 존재 여부 확인
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FOOD_TRUCK_NOT_FOUND));

        if (request.isSavedRequest()) { // 저장 요청
            // 이미 저장된 푸드트럭인지 확인
            if(savedFoodTruckRepository.existsByMemberIdAndFoodTruckId(userId, foodTruckId)) {
                throw new BusinessException(ErrorCode.FOOD_TRUCK_ALREADY_SAVED);
            }

            SavedFoodTruck savedFoodTruck = SavedFoodTruck.of(user, foodTruck);
            savedFoodTruckRepository.save(savedFoodTruck);
        } else { // 저장 취소 요청
            SavedFoodTruck savedFoodTruck = savedFoodTruckRepository.findByUserIdAndFoodTruckId(userId, foodTruckId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_TRUCK_NOT_SAVED));

            savedFoodTruckRepository.delete(savedFoodTruck);
        }

        return new SavedFoodTruckResponse(request.isSavedRequest());
    }
}
