package konkuk.chacall.domain.foodtruck.application;

import konkuk.chacall.domain.foodtruck.application.image.FoodTruckImageService;
import konkuk.chacall.domain.foodtruck.application.search.FoodTruckSearchService;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.FoodTruckSearchRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.ImageRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.FoodTruckResponse;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.ImageResponse;
import konkuk.chacall.domain.owner.application.validator.OwnerValidator;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FoodTruckService {

    private final FoodTruckSearchService foodTruckSearchService;
    private final FoodTruckImageService foodTruckImageService;

    private final OwnerValidator ownerValidator;

    public CursorPagingResponse<FoodTruckResponse> getFoodTrucks(FoodTruckSearchRequest request) {
        return foodTruckSearchService.getFoodTrucks(request);
    }

    public ImageResponse createFoodTruckImagePresignedUrl(ImageRequest request, Long ownerId) {
        User owner = ownerValidator.validateAndGetOwner(ownerId);

        return foodTruckImageService.createFoodTruckImagePresignedUrl(request, owner);
    }

    public ImageResponse createMenuImagePresignedUrl(ImageRequest request, Long ownerId) {
        User owner = ownerValidator.validateAndGetOwner(ownerId);

        return foodTruckImageService.createMenuImagePresignedUrl(request, owner);
    }
}
