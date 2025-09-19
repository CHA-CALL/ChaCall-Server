package konkuk.chacall.domain.member.application.foodtruck;

import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.member.domain.SavedFoodTruck;
import konkuk.chacall.domain.member.domain.repository.SavedFoodTruckRepository;
import konkuk.chacall.domain.member.presentation.dto.request.UpdateFoodTruckSaveStatusRequest;
import konkuk.chacall.domain.member.presentation.dto.response.SavedFoodTruckResponse;
import konkuk.chacall.domain.member.presentation.dto.response.SavedFoodTruckStatusResponse;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedFoodTruckService {

    private final SavedFoodTruckRepository savedFoodTruckRepository;
    private final FoodTruckRepository foodTruckRepository;

    public SavedFoodTruckStatusResponse updateFoodTruckSaveStatus(UpdateFoodTruckSaveStatusRequest request, Long foodTruckId, User member) {

        // 푸드트럭 존재 여부 확인
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FOOD_TRUCK_NOT_FOUND));

        if (request.isSavedRequest()) { // 저장 요청
            // 이미 저장된 푸드트럭인지 확인
            if(savedFoodTruckRepository.existsByMemberAndFoodTruck(member, foodTruck)) {
                throw new BusinessException(ErrorCode.SAVED_FOOD_TRUCK_ALREADY_EXIST);
            }

            SavedFoodTruck savedFoodTruck = SavedFoodTruck.of(member, foodTruck);
            savedFoodTruckRepository.save(savedFoodTruck);
        } else { // 저장 취소 요청
            SavedFoodTruck savedFoodTruck = savedFoodTruckRepository.findByMemberAndFoodTruck(member, foodTruck)
                    .orElseThrow(() -> new BusinessException(ErrorCode.SAVED_FOOD_TRUCK_NOT_FOUND));

            savedFoodTruckRepository.delete(savedFoodTruck);
        }

        return new SavedFoodTruckStatusResponse(request.isSavedRequest());
    }

    public CursorPagingResponse<SavedFoodTruckResponse> getSavedFoodTrucks(CursorPagingRequest cursorPagingRequest, User member) {
        // 저장된 푸드트럭 목록 조회
        Slice<SavedFoodTruck> savedFoodTruckSlice = savedFoodTruckRepository
                .findMemberSavedFoodTruckWithCursor(member, cursorPagingRequest.cursor(), PageRequest.of(0, cursorPagingRequest.size()));
        List<SavedFoodTruck> savedFoodTrucks = savedFoodTruckSlice.getContent();

        // 응답 DTO로 변환
        List<SavedFoodTruckResponse> responses = savedFoodTrucks.stream()
                .map(SavedFoodTruck::getFoodTruck)
                .map(SavedFoodTruckResponse::of)
                .toList();

        return CursorPagingResponse.of(responses, SavedFoodTruckResponse::foodTruckId, savedFoodTruckSlice.hasNext());
    }
}
