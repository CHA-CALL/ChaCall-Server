package konkuk.chacall.domain.user.application.admin;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckDocumentRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckStatus;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckInfo;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.domain.user.presentation.dto.request.ApproveFoodTruckStatusRequest;
import konkuk.chacall.domain.user.presentation.dto.response.FoodTruckForAdminResponse;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
@DisplayName("AdminService 테스트")
class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private FoodTruckRepository foodTruckRepository;

    @Mock
    private FoodTruckDocumentRepository foodTruckDocumentRepository;

    @Mock
    private FoodTruck foodTruck;

    @Mock
    private User owner;

    @Nested
    @DisplayName("푸드트럭 상태 승인/반려 시나리오")
    class ApproveFoodTruckStatusScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상 요청 시 푸드트럭의 상태를 성공적으로 변경한다")
            void approveStatusSuccess() {
                // given
                ApproveFoodTruckStatusRequest request = new ApproveFoodTruckStatusRequest(FoodTruckStatus.APPROVED);
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                doNothing().when(foodTruck).approveFoodTruck(any(FoodTruckStatus.class));

                // when
                adminService.approveFoodTruckStatus(1L, request);

                // then
                verify(foodTruckRepository).findById(anyLong());
                verify(foodTruck).approveFoodTruck(eq(FoodTruckStatus.APPROVED));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 푸드트럭 ID로 요청 시 FOOD_TRUCK_NOT_FOUND 예외를 발생시킨다")
            void approveStatusWithInvalidIdFail() {
                // given
                ApproveFoodTruckStatusRequest request = new ApproveFoodTruckStatusRequest(FoodTruckStatus.APPROVED);
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.empty());

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> adminService.approveFoodTruckStatus(99L, request));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FOOD_TRUCK_NOT_FOUND);
            }
        }
    }

    @Nested
    @DisplayName("관리자용 푸드트럭 목록 조회 시나리오")
    class GetAllFoodTrucksScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("상태 필터 없이 요청 시 모든 푸드트럭 목록을 반환한다")
            void getAllFoodTrucksWithoutFilterSuccess() {
                // given
                FoodTruckInfo foodTruckInfo = mock(FoodTruckInfo.class);

                given(foodTruckRepository.findAll()).willReturn(Collections.singletonList(foodTruck));
                given(foodTruckDocumentRepository.findAllInFoodTruckIds(anyList())).willReturn(Collections.emptyList());
                given(foodTruck.getFoodTruckId()).willReturn(1L);
                given(foodTruck.getFoodTruckInfo()).willReturn(foodTruckInfo);
                given(foodTruckInfo.getName()).willReturn("푸드트럭");
                given(foodTruck.getOwner()).willReturn(owner);
                given(owner.getName()).willReturn("사장님");
                given(foodTruck.getFoodTruckStatus()).willReturn(FoodTruckStatus.PENDING);

                // when
                List<FoodTruckForAdminResponse> responses = adminService.getAllFoodTrucks(null);

                // then
                assertThat(responses).hasSize(1);
                verify(foodTruckRepository).findAll();
            }

            @Test
            @DisplayName("상태 필터와 함께 요청 시 해당 상태의 푸드트럭 목록만 반환한다")
            void getAllFoodTrucksWithFilterSuccess() {
                // given
                FoodTruckStatus status = FoodTruckStatus.PENDING;
                FoodTruckInfo foodTruckInfo = mock(FoodTruckInfo.class);

                given(foodTruckRepository.findAllByFoodTruckStatus(any(FoodTruckStatus.class)))
                        .willReturn(Collections.singletonList(foodTruck));
                given(foodTruckDocumentRepository.findAllInFoodTruckIds(anyList())).willReturn(Collections.emptyList());
                given(foodTruck.getFoodTruckId()).willReturn(1L);
                given(foodTruck.getFoodTruckInfo()).willReturn(foodTruckInfo);
                given(foodTruckInfo.getName()).willReturn("푸드트럭");
                given(foodTruck.getOwner()).willReturn(owner);
                given(owner.getName()).willReturn("사장님");
                given(foodTruck.getFoodTruckStatus()).willReturn(status);


                // when
                List<FoodTruckForAdminResponse> responses = adminService.getAllFoodTrucks(status.getDescription());

                // then
                assertThat(responses).hasSize(1);
                verify(foodTruckRepository).findAllByFoodTruckStatus(eq(status));
            }
        }
    }
}
