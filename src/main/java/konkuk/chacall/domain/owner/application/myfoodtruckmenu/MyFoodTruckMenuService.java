package konkuk.chacall.domain.owner.application.myfoodtruckmenu;

import konkuk.chacall.domain.foodtruck.domain.model.Menu;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.MenuRepository;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckStatus;
import konkuk.chacall.domain.owner.presentation.dto.request.MyFoodTruckMenuListRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MyFoodTruckMenuService {

    private final FoodTruckRepository foodTruckRepository;
    private final MenuRepository menuRepository;

    public CursorPagingResponse<MyFoodTruckMenuResponse> getMyFoodTruckMenus(Long foodTruckId, MyFoodTruckMenuListRequest request) {
        SortType sort = SortType.fromNullable(request.sort());
        CursorPagingRequest pagingRequest = request.pagingOrDefault(sort);
        Pageable pageable = PageRequest.of(0, pagingRequest.size());

        // 푸드트럭 상태 검증
        if(!foodTruckRepository.existsByFoodTruckIdAndFoodTruckStatusIn(foodTruckId, List.of(FoodTruckStatus.ON, FoodTruckStatus.OFF))) {
            throw new BusinessException(ErrorCode.FOOD_TRUCK_NOT_APPROVED);
        }

        Slice<Menu> menuSlice = switch (sort) {
            case NEWEST -> menuRepository.findMenusDesc(foodTruckId, pagingRequest.cursor(), pageable);
            case OLDEST -> menuRepository.findMenusAsc(foodTruckId, pagingRequest.cursor(), pageable);
        };

        List<MyFoodTruckMenuResponse> content = menuSlice.getContent().stream()
                .map(MyFoodTruckMenuResponse::from)
                .toList();

        return CursorPagingResponse.of(content, MyFoodTruckMenuResponse::menuId, menuSlice.hasNext());
    }
}
