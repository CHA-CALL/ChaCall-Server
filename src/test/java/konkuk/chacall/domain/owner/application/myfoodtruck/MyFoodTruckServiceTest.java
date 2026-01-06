package konkuk.chacall.domain.owner.application.myfoodtruck;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.AvailableDateRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckDocumentRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckServiceAreaRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.MenuRepository;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckStatus;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckViewedStatus;
import konkuk.chacall.domain.member.domain.repository.RatingRepository;
import konkuk.chacall.domain.member.domain.repository.SavedFoodTruckRepository;
import konkuk.chacall.domain.owner.presentation.dto.request.FoodTruckCreateRequest;
import konkuk.chacall.domain.owner.presentation.dto.request.UpdateFoodTruckViewedStatusRequest;
import konkuk.chacall.domain.reservation.domain.repository.ReservationRepository;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.AuthException;
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

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MyFoodTruckService 테스트")
class MyFoodTruckServiceTest {

    @InjectMocks
    private MyFoodTruckService myFoodTruckService;

    @Mock private FoodTruckRepository foodTruckRepository;
    @Mock private FoodTruckServiceAreaRepository foodTruckServiceAreaRepository;
    @Mock private MenuRepository menuRepository;
    @Mock private ReservationRepository reservationRepository;
    @Mock private SavedFoodTruckRepository savedFoodTruckRepository;
    @Mock private AvailableDateRepository availableDateRepository;
    @Mock private RatingRepository ratingRepository;
    @Mock private FoodTruckDocumentRepository foodTruckDocumentRepository;

    @Mock private User owner;
    @Mock private FoodTruck foodTruck;

    @Nested
    @DisplayName("내 푸드트럭 삭제 시나리오")
    class DeleteMyFoodTruckScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상 요청 시 푸드트럭과 모든 관련 데이터를 삭제한다")
            void deleteMyFoodTruckSuccess() {
                // given
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                doNothing().when(foodTruck).validateOwner(anyLong());

                // when
                myFoodTruckService.deleteMyFoodTruck(1L, 1L);

                // then
                verify(foodTruckRepository).findById(anyLong());
                verify(foodTruck).validateOwner(anyLong());
                verify(foodTruckDocumentRepository).deleteAllByFoodTruckId(anyLong());
                verify(foodTruckServiceAreaRepository).deleteAllByFoodTruckId(anyLong());
                verify(menuRepository).deleteAllByFoodTruckId(anyLong());
                verify(availableDateRepository).deleteAllByFoodTruckId(anyLong());
                verify(ratingRepository).deleteAllByFoodTruckId(anyLong());
                verify(reservationRepository).deleteAllByFoodTruckId(anyLong());
                verify(savedFoodTruckRepository).deleteAllByFoodTruckId(anyLong());
                verify(foodTruckRepository).delete(any(FoodTruck.class));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 푸드트럭 ID로 요청 시 FOOD_TRUCK_NOT_FOUND 예외를 발생시킨다")
            void deleteWithInvalidIdFail() {
                // given
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.empty());

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> myFoodTruckService.deleteMyFoodTruck(1L, 99L));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FOOD_TRUCK_NOT_FOUND);
            }

            @Test
            @DisplayName("소유주가 아닌 사용자가 요청 시 AUTH_FORBIDDEN 예외를 발생시킨다")
            void deleteByNotOwnerFail() {
                // given
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                doThrow(new AuthException(ErrorCode.USER_FORBIDDEN)).when(foodTruck).validateOwner(anyLong());

                // when
                AuthException ex = assertThrows(AuthException.class,
                        () -> myFoodTruckService.deleteMyFoodTruck(2L, 1L));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_FORBIDDEN);
            }
        }
    }

    @Nested
    @DisplayName("푸드트럭 공개 상태 변경 시나리오")
    class UpdateFoodTruckViewedStatusScenario {

        private final UpdateFoodTruckViewedStatusRequest request = new UpdateFoodTruckViewedStatusRequest(FoodTruckViewedStatus.ON);

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("승인된 푸드트럭의 공개 상태를 성공적으로 변경한다")
            void updateStatusSuccess() {
                // given
                given(foodTruckRepository.findByFoodTruckIdAndOwnerIdAndFoodTruckStatus(anyLong(), anyLong(), eq(FoodTruckStatus.APPROVED)))
                        .willReturn(Optional.of(foodTruck));
                doNothing().when(foodTruck).changeViewedStatus(any(FoodTruckViewedStatus.class));

                // when
                myFoodTruckService.updateFoodTruckViewedStatus(1L, 1L, request);

                // then
                verify(foodTruckRepository).findByFoodTruckIdAndOwnerIdAndFoodTruckStatus(anyLong(), anyLong(), any(FoodTruckStatus.class));
                verify(foodTruck).changeViewedStatus(any(FoodTruckViewedStatus.class));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("승인되지 않았거나 소유주가 아닌 푸드트럭에 요청 시 FOOD_TRUCK_NOT_APPROVED 예외를 발생시킨다")
            void updateStatusOnNotApprovedFail() {
                // given
                given(foodTruckRepository.findByFoodTruckIdAndOwnerIdAndFoodTruckStatus(anyLong(), anyLong(), eq(FoodTruckStatus.APPROVED)))
                        .willReturn(Optional.empty());

                // when
                BusinessException ex = assertThrows(BusinessException.class,
                        () -> myFoodTruckService.updateFoodTruckViewedStatus(1L, 1L, request));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FOOD_TRUCK_NOT_APPROVED);
            }
        }
    }

    @Nested
    @DisplayName("새로운 푸드트럭 생성(신청) 시나리오")
    class CreateNewFoodTruckScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상 요청 시 푸드트럭과 관련 서류 엔티티를 생성한다")
            void createNewFoodTruckSuccess() {
                // given
                FoodTruckCreateRequest request = new FoodTruckCreateRequest("새 푸드트럭", "url1", Collections.emptyList());
                given(foodTruckRepository.save(any(FoodTruck.class))).willReturn(foodTruck);

                // when
                myFoodTruckService.createNewFoodTruck(owner, request);

                // then
                verify(foodTruckRepository).save(any(FoodTruck.class));
                verify(foodTruckDocumentRepository).save(any());
                verify(foodTruckDocumentRepository).saveAll(any());
            }
        }
    }
}
