package konkuk.chacall.domain.foodtruck.application.menu;

import konkuk.chacall.domain.foodtruck.domain.model.Menu;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.MenuRepository;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckStatus;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.FoodTruckMenuRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.FoodTruckMenuResponse;
import konkuk.chacall.domain.owner.presentation.dto.response.MyFoodTruckMenuResponse;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.dto.SortType;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodTruckMenuService {

    private final MenuRepository menuRepository;

    public CursorPagingResponse<FoodTruckMenuResponse> getFoodTruckMenus(Long foodTruckId, FoodTruckMenuRequest request) {
        SortType sort = SortType.fromNullable(request.sort());
        CursorPagingRequest pagingRequest = request.pagingOrDefault(sort);
        Pageable pageable = PageRequest.of(0, pagingRequest.size());

        Slice<Menu> menuSlice = switch (sort) {
            case NEWEST -> menuRepository.findVisibleMenusDesc(foodTruckId, pagingRequest.cursor(), pageable);
            case OLDEST -> menuRepository.findVisibleMenusAsc(foodTruckId, pagingRequest.cursor(), pageable);
        };

        List<FoodTruckMenuResponse> content = menuSlice.getContent().stream()
                .map(FoodTruckMenuResponse::from)
                .toList();

        return CursorPagingResponse.of(content, FoodTruckMenuResponse::menuId, menuSlice.hasNext());
    }
}
