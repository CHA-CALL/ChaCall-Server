package konkuk.chacall.domain.foodtruck.application.menu;

import konkuk.chacall.domain.foodtruck.domain.model.Menu;
import konkuk.chacall.domain.foodtruck.domain.repository.MenuRepository;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.MenuListRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.MenuResponse;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.dto.SortType;
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
public class MenuService {

    private final MenuRepository menuRepository;

    public CursorPagingResponse<MenuResponse> getMenus(Long foodTruckId, MenuListRequest request) {
        SortType sort = SortType.fromNullable(request.sort());
        CursorPagingRequest pagingRequest = request.pagingOrDefault(sort);
        Pageable pageable = PageRequest.of(0, pagingRequest.size());

        Slice<Menu> menuSlice = switch (sort) {
            case NEWEST -> menuRepository.findMenusDesc(foodTruckId, pagingRequest.cursor(), pageable);
            case OLDEST -> menuRepository.findMenusAsc(foodTruckId, pagingRequest.cursor(), pageable);
        };

        List<MenuResponse> content = menuSlice.getContent().stream()
                .map(MenuResponse::from)
                .toList();

        return CursorPagingResponse.of(content, MenuResponse::menuId, menuSlice.hasNext());
    }
}
