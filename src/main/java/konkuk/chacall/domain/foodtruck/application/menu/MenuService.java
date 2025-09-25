package konkuk.chacall.domain.foodtruck.application.menu;

import konkuk.chacall.domain.foodtruck.domain.model.Menu;
import konkuk.chacall.domain.foodtruck.domain.repository.MenuRepository;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.MenuListRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.MenuResponse;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;

    public CursorPagingResponse<MenuResponse> getMenus(Long foodTruckId, MenuListRequest request) {
    }
}
