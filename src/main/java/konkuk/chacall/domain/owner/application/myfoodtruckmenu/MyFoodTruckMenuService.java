package konkuk.chacall.domain.owner.application.myfoodtruckmenu;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.model.Menu;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.MenuRepository;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckStatus;
import konkuk.chacall.domain.foodtruck.domain.value.MenuViewedStatus;
import konkuk.chacall.domain.owner.presentation.dto.request.MyFoodTruckMenuListRequest;
import konkuk.chacall.domain.owner.presentation.dto.request.RegisterMenuRequest;
import konkuk.chacall.domain.owner.presentation.dto.request.UpdateMenuStatusRequest;
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

@RequiredArgsConstructor
@Service
public class MyFoodTruckMenuService {

    private final FoodTruckRepository foodTruckRepository;
    private final MenuRepository menuRepository;

    public CursorPagingResponse<MyFoodTruckMenuResponse> getMyFoodTruckMenus(Long ownerId, Long foodTruckId, MyFoodTruckMenuListRequest request) {
        SortType sort = SortType.fromNullable(request.sort());
        CursorPagingRequest pagingRequest = request.pagingOrDefault(sort);
        Pageable pageable = PageRequest.of(0, pagingRequest.size());

        // 본인 소유인지, 푸드트럭이 승인 완료된 상태인지 검증
        if(!foodTruckRepository.existsByFoodTruckIdAndOwnerIdAndFoodTruckStatusIn(ownerId, foodTruckId, List.of(FoodTruckStatus.ON, FoodTruckStatus.OFF))) {
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

    public void registerMenu(Long ownerId, Long foodTruckId, RegisterMenuRequest request) {

        // 본인 소유인지, 푸드트럭이 승인 완료된 상태인지 검증
        FoodTruck foodTruck = foodTruckRepository.findByFoodTruckIdAndOwnerIdAndFoodTruckStatusIn(
                foodTruckId, ownerId, List.of(FoodTruckStatus.ON, FoodTruckStatus.OFF))
                .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_TRUCK_NOT_APPROVED));

        Menu menu = Menu.create(
                request.name(),
                request.price(),
                request.description(),
                request.photoUrl(),
                foodTruck);

        menuRepository.save(menu);
    }

    public void updateMenuStatus(Long ownerId, Long foodTruckId, Long menuId, UpdateMenuStatusRequest request) {

        // 본인 소유인지, 푸드트럭이 승인 완료된 상태인지 검증
        if(!foodTruckRepository.existsByFoodTruckIdAndOwnerIdAndFoodTruckStatusIn(ownerId, foodTruckId, List.of(FoodTruckStatus.ON, FoodTruckStatus.OFF))) {
            throw new BusinessException(ErrorCode.FOOD_TRUCK_NOT_APPROVED);
        }

        // 메뉴 존재 여부 확인
        Menu menu = menuRepository.findByMenuIdAndFoodTruckId(menuId, foodTruckId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MENU_NOT_FOUND));

        // 메뉴 표시 여부 변경 및 상태 전이 검증
        menu.changeViewedStatus(request.status());
    }
}
