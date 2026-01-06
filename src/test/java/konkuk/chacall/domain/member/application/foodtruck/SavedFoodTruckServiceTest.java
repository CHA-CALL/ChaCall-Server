package konkuk.chacall.domain.member.application.foodtruck;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.member.domain.SavedFoodTruck;
import konkuk.chacall.domain.member.domain.repository.SavedFoodTruckRepository;
import konkuk.chacall.domain.member.presentation.dto.request.UpdateFoodTruckSaveStatusRequest;
import konkuk.chacall.domain.member.presentation.dto.response.SavedFoodTruckStatusResponse;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("SavedFoodTruckService 테스트")
class SavedFoodTruckServiceTest {

    @InjectMocks
    private SavedFoodTruckService savedFoodTruckService;

    @Mock
    private SavedFoodTruckRepository savedFoodTruckRepository;

    @Mock
    private FoodTruckRepository foodTruckRepository;

    @Mock
    private User member;

    @Mock
    private FoodTruck foodTruck;

    @Mock
    private SavedFoodTruck savedFoodTruck;


    @Nested
    @DisplayName("푸드트럭 저장 상태 업데이트 시나리오")
    class UpdateFoodTruckSaveStatusScenario {

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("저장 요청 시, 아직 저장되지 않은 푸드트럭을 성공적으로 저장한다")
            void saveFoodTruckSuccess() {
                // given
                UpdateFoodTruckSaveStatusRequest request = new UpdateFoodTruckSaveStatusRequest(true);
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                given(savedFoodTruckRepository.existsByMemberAndFoodTruck(any(User.class), any(FoodTruck.class))).willReturn(false);
                given(savedFoodTruckRepository.save(any(SavedFoodTruck.class))).willReturn(savedFoodTruck);

                // when
                SavedFoodTruckStatusResponse response = savedFoodTruckService.updateFoodTruckSaveStatus(request, 1L, member);

                // then
                assertThat(response.isSaved()).isTrue();
                verify(savedFoodTruckRepository).save(any(SavedFoodTruck.class));
            }

            @Test
            @DisplayName("저장 취소 요청 시, 저장되어 있던 푸드트럭을 성공적으로 저장 취소한다")
            void unsaveFoodTruckSuccess() {
                // given
                UpdateFoodTruckSaveStatusRequest request = new UpdateFoodTruckSaveStatusRequest(false);
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                given(savedFoodTruckRepository.findByMemberAndFoodTruck(any(User.class), any(FoodTruck.class))).willReturn(Optional.of(savedFoodTruck));
                doNothing().when(savedFoodTruckRepository).delete(any(SavedFoodTruck.class));

                // when
                SavedFoodTruckStatusResponse response = savedFoodTruckService.updateFoodTruckSaveStatus(request, 1L, member);

                // then
                assertThat(response.isSaved()).isFalse();
                verify(savedFoodTruckRepository).delete(any(SavedFoodTruck.class));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 푸드트럭 ID로 요청 시 FOOD_TRUCK_NOT_FOUND 예외를 발생시킨다")
            void updateStatusForInvalidFoodTruckFail() {
                // given
                UpdateFoodTruckSaveStatusRequest request = new UpdateFoodTruckSaveStatusRequest(true);
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.empty());

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> savedFoodTruckService.updateFoodTruckSaveStatus(request, 99L, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FOOD_TRUCK_NOT_FOUND);
            }

            @Test
            @DisplayName("저장 요청 시, 이미 저장된 푸드트럭이면 SAVED_FOOD_TRUCK_ALREADY_EXIST 예외를 발생시킨다")
            void saveAlreadySavedFoodTruckFail() {
                // given
                UpdateFoodTruckSaveStatusRequest request = new UpdateFoodTruckSaveStatusRequest(true);
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                given(savedFoodTruckRepository.existsByMemberAndFoodTruck(any(User.class), any(FoodTruck.class))).willReturn(true);

                // when
                BusinessException ex = assertThrows(BusinessException.class,
                        () -> savedFoodTruckService.updateFoodTruckSaveStatus(request, 1L, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.SAVED_FOOD_TRUCK_ALREADY_EXIST);
            }

            @Test
            @DisplayName("저장 취소 요청 시, 저장되지 않은 푸드트럭이면 SAVED_FOOD_TRUCK_NOT_FOUND 예외를 발생시킨다")
            void unsaveNotSavedFoodTruckFail() {
                // given
                UpdateFoodTruckSaveStatusRequest request = new UpdateFoodTruckSaveStatusRequest(false);
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                given(savedFoodTruckRepository.findByMemberAndFoodTruck(any(User.class), any(FoodTruck.class))).willReturn(Optional.empty());

                // when
                BusinessException ex = assertThrows(BusinessException.class,
                        () -> savedFoodTruckService.updateFoodTruckSaveStatus(request, 1L, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.SAVED_FOOD_TRUCK_NOT_FOUND);
            }
        }
    }
}
