package konkuk.chacall.domain.foodtruck.application.menu;

import konkuk.chacall.domain.foodtruck.domain.model.Menu;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.MenuRepository;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.FoodTruckMenuRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.FoodTruckMenuResponse;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.dto.SortType;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("FoodTruckMenuService 테스트")
class FoodTruckMenuServiceTest {

    @InjectMocks
    private FoodTruckMenuService foodTruckMenuService;

    @Mock
    private FoodTruckRepository foodTruckRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private User member;

    @Mock
    private Menu menu1;

    @Mock
    private Menu menu2;

    @Nested
    @DisplayName("푸드트럭 메뉴 목록 조회 시나리오")
    class GetFoodTruckMenusScenario {

        private FoodTruckMenuRequest createRequest(SortType sortType, Long cursor) {
            return new FoodTruckMenuRequest(sortType, new CursorPagingRequest(cursor, 10));
        }

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("최신순 정렬 요청 시 메뉴 목록을 페이지네이션하여 반환한다")
            void getMenusSortByNewestSuccess() {
                // given
                FoodTruckMenuRequest request = createRequest(SortType.NEWEST, 10L);
                Slice<Menu> menuSlice = new SliceImpl<>(List.of(menu1, menu2), Pageable.unpaged(), true);

                given(foodTruckRepository.existsById(anyLong())).willReturn(true);
                given(menuRepository.findVisibleMenusDesc(anyLong(), anyLong(), any(Pageable.class))).willReturn(menuSlice);

                // when
                CursorPagingResponse<FoodTruckMenuResponse> response = foodTruckMenuService.getFoodTruckMenus(1L, request);

                // then
                assertThat(response.content()).hasSize(2);
                assertThat(response.hasNext()).isTrue();
                verify(foodTruckRepository).existsById(anyLong());
                verify(menuRepository).findVisibleMenusDesc(anyLong(), anyLong(), any(Pageable.class));
            }

            @Test
            @DisplayName("오래된순 정렬 요청 시 메뉴 목록을 페이지네이션하여 반환한다")
            void getMenusSortByOldestSuccess() {
                // given
                FoodTruckMenuRequest request = createRequest(SortType.OLDEST, 1L);
                Slice<Menu> menuSlice = new SliceImpl<>(List.of(menu1, menu2), Pageable.unpaged(), false);

                given(foodTruckRepository.existsById(anyLong())).willReturn(true);
                given(menuRepository.findVisibleMenusAsc(anyLong(), anyLong(), any(Pageable.class))).willReturn(menuSlice);

                // when
                CursorPagingResponse<FoodTruckMenuResponse> response = foodTruckMenuService.getFoodTruckMenus(1L, request);

                // then
                assertThat(response.content()).hasSize(2);
                assertThat(response.hasNext()).isFalse();
                verify(foodTruckRepository).existsById(anyLong());
                verify(menuRepository).findVisibleMenusAsc(anyLong(), anyLong(), any(Pageable.class));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 푸드트럭 ID로 요청 시 FOOD_TRUCK_NOT_FOUND 예외를 발생시킨다")
            void getMenusWithInvalidFoodTruckIdFail() {
                // given
                FoodTruckMenuRequest request = createRequest(SortType.NEWEST, 10L);
                given(foodTruckRepository.existsById(anyLong())).willReturn(false);

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> foodTruckMenuService.getFoodTruckMenus(99L, request));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FOOD_TRUCK_NOT_FOUND);
            }
        }
    }

    @Nested
    @DisplayName("푸드트럭 메뉴 검색 시나리오")
    class SearchFoodTruckMenusScenario {

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("키워드로 메뉴 검색 시 일치하는 메뉴 목록을 반환한다")
            void searchMenusByKeywordSuccess() {
                // given
                String keyword = "떡볶이";
                List<Menu> menus = List.of(menu1);
                given(foodTruckRepository.existsById(anyLong())).willReturn(true);
                given(menuRepository.searchByKeyword(anyLong(), any(String.class))).willReturn(menus);

                // when
                List<FoodTruckMenuResponse> responses = foodTruckMenuService.searchFoodTruckMenus(1L, keyword, member);

                // then
                assertThat(responses).hasSize(1);
                verify(foodTruckRepository).existsById(anyLong());
                verify(menuRepository).searchByKeyword(anyLong(), any(String.class));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 푸드트럭 ID로 요청 시 FOOD_TRUCK_NOT_FOUND 예외를 발생시킨다")
            void searchMenusWithInvalidFoodTruckIdFail() {
                // given
                String keyword = "떡볶이";
                given(foodTruckRepository.existsById(anyLong())).willReturn(false);

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> foodTruckMenuService.searchFoodTruckMenus(99L, keyword, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FOOD_TRUCK_NOT_FOUND);
            }
        }
    }
}
