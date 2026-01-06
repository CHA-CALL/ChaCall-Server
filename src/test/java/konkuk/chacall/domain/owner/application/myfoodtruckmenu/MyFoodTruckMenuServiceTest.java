package konkuk.chacall.domain.owner.application.myfoodtruckmenu;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.model.Menu;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.MenuRepository;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckStatus;
import konkuk.chacall.domain.foodtruck.domain.value.MenuViewedStatus;
import konkuk.chacall.domain.owner.presentation.dto.request.RegisterMenuRequest;
import konkuk.chacall.domain.owner.presentation.dto.request.UpdateMenuRequest;
import konkuk.chacall.domain.owner.presentation.dto.request.UpdateMenuStatusRequest;
import konkuk.chacall.global.common.exception.BusinessException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MyFoodTruckMenuService 테스트")
class MyFoodTruckMenuServiceTest {

    @InjectMocks
    private MyFoodTruckMenuService myFoodTruckMenuService;

    @Mock
    private FoodTruckRepository foodTruckRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private FoodTruck foodTruck;

    @Mock
    private Menu menu;

    private void givenOwnerAndApprovedFoodTruck() {
        given(foodTruckRepository.findByFoodTruckIdAndOwnerIdAndFoodTruckStatus(anyLong(), anyLong(), eq(FoodTruckStatus.APPROVED)))
                .willReturn(Optional.of(foodTruck));
    }

    private void givenOwnerAndApprovedFoodTruckExists() {
        given(foodTruckRepository.existsByFoodTruckIdAndOwnerIdAndFoodTruckStatus(anyLong(), anyLong(), eq(FoodTruckStatus.APPROVED)))
                .willReturn(true);
    }

    private void givenNotOwnerOrNotApprovedFoodTruck() {
        given(foodTruckRepository.existsByFoodTruckIdAndOwnerIdAndFoodTruckStatus(anyLong(), anyLong(), eq(FoodTruckStatus.APPROVED)))
                .willReturn(false);
    }

    @Nested
    @DisplayName("메뉴 등록 시나리오")
    class RegisterMenuScenario {

        private final RegisterMenuRequest request = new RegisterMenuRequest("메뉴", "설명", 10000, "url");

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상 요청 시 메뉴를 성공적으로 등록한다")
            void registerMenuSuccess() {
                // given
                givenOwnerAndApprovedFoodTruck();
                given(menuRepository.save(any(Menu.class))).willReturn(menu);

                // when
                myFoodTruckMenuService.registerMenu(1L, 1L, request);

                // then
                verify(menuRepository).save(any(Menu.class));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("소유주가 아니거나 승인되지 않은 푸드트럭이면 FOOD_TRUCK_NOT_APPROVED 예외를 발생시킨다")
            void registerMenuForNotApprovedFoodTruckFail() {
                // given
                given(foodTruckRepository.findByFoodTruckIdAndOwnerIdAndFoodTruckStatus(anyLong(), anyLong(), eq(FoodTruckStatus.APPROVED)))
                        .willReturn(Optional.empty());

                // when
                BusinessException ex = assertThrows(BusinessException.class,
                        () -> myFoodTruckMenuService.registerMenu(1L, 1L, request));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FOOD_TRUCK_NOT_APPROVED);
            }
        }
    }

    @Nested
    @DisplayName("메뉴 상태 수정 시나리오")
    class UpdateMenuStatusScenario {
        private final UpdateMenuStatusRequest request = new UpdateMenuStatusRequest(MenuViewedStatus.ON);

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상 요청 시 메뉴 상태를 성공적으로 수정한다")
            void updateMenuStatusSuccess() {
                // given
                givenOwnerAndApprovedFoodTruckExists();
                given(menuRepository.findByMenuIdAndFoodTruckId(anyLong(), anyLong())).willReturn(Optional.of(menu));
                doNothing().when(menu).changeViewedStatus(any(MenuViewedStatus.class));

                // when
                myFoodTruckMenuService.updateMenuStatus(1L, 1L, 1L, request);

                // then
                verify(menu).changeViewedStatus(any(MenuViewedStatus.class));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 메뉴 ID 요청 시 MENU_NOT_FOUND 예외를 발생시킨다")
            void updateStatusWithInvalidMenuIdFail() {
                // given
                givenOwnerAndApprovedFoodTruckExists();
                given(menuRepository.findByMenuIdAndFoodTruckId(anyLong(), anyLong())).willReturn(Optional.empty());

                // when
                BusinessException ex = assertThrows(BusinessException.class,
                        () -> myFoodTruckMenuService.updateMenuStatus(1L, 1L, 99L, request));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MENU_NOT_FOUND);
            }
        }
    }

    @Nested
    @DisplayName("메뉴 정보 수정 시나리오")
    class UpdateMenuScenario {
        private final UpdateMenuRequest request = new UpdateMenuRequest("새메뉴", "새설명", 12000, "new_url");

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상 요청 시 메뉴 정보를 성공적으로 수정한다")
            void updateMenuSuccess() {
                // given
                givenOwnerAndApprovedFoodTruckExists();
                given(menuRepository.findByMenuIdAndFoodTruckId(anyLong(), anyLong())).willReturn(Optional.of(menu));
                doNothing().when(menu).updateMenu(anyString(), anyInt(), anyString(), anyString());

                // when
                myFoodTruckMenuService.updateMenu(1L, 1L, 1L, request);

                // then
                verify(menu).updateMenu(anyString(), anyInt(), anyString(), anyString());
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("소유주가 아니거나 승인되지 않은 푸드트럭이면 FOOD_TRUCK_NOT_APPROVED 예외를 발생시킨다")
            void updateMenuForNotApprovedFoodTruckFail() {
                // given
                givenNotOwnerOrNotApprovedFoodTruck();

                // when
                BusinessException ex = assertThrows(BusinessException.class,
                        () -> myFoodTruckMenuService.updateMenu(1L, 1L, 1L, request));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FOOD_TRUCK_NOT_APPROVED);
            }
        }
    }

    @Nested
    @DisplayName("메뉴 삭제 시나리오")
    class DeleteMenuScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상 요청 시 메뉴를 성공적으로 삭제한다")
            void deleteMenuSuccess() {
                // given
                givenOwnerAndApprovedFoodTruckExists();
                given(menuRepository.findByMenuIdAndFoodTruckId(anyLong(), anyLong())).willReturn(Optional.of(menu));
                doNothing().when(menuRepository).delete(any(Menu.class));

                // when
                myFoodTruckMenuService.deleteMenu(1L, 1L, 1L);

                // then
                verify(menuRepository).delete(menu);
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 메뉴 ID 요청 시 MENU_NOT_FOUND 예외를 발생시킨다")
            void deleteWithInvalidMenuIdFail() {
                // given
                givenOwnerAndApprovedFoodTruckExists();
                given(menuRepository.findByMenuIdAndFoodTruckId(anyLong(), anyLong())).willReturn(Optional.empty());

                // when
                BusinessException ex = assertThrows(BusinessException.class,
                        () -> myFoodTruckMenuService.deleteMenu(1L, 1L, 99L));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MENU_NOT_FOUND);
            }
        }
    }
}
