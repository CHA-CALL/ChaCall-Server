package konkuk.chacall.domain.foodtruck.application;

import konkuk.chacall.domain.foodtruck.application.image.FoodTruckImageService;
import konkuk.chacall.domain.foodtruck.application.info.FoodTruckInfoService;
import konkuk.chacall.domain.foodtruck.application.menu.FoodTruckMenuService;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.*;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.*;
import konkuk.chacall.domain.owner.application.validator.OwnerValidator;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.domain.member.application.validator.MemberValidator;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FoodTruckService {

    private final FoodTruckInfoService foodTruckInfoService;
    private final FoodTruckMenuService foodTruckMenuService;
    private final FoodTruckImageService foodTruckImageService;

    private final MemberValidator memberValidator;
    private final OwnerValidator ownerValidator;

    public CursorPagingResponse<FoodTruckResponse> getFoodTrucks(Long memberId, FoodTruckSearchRequest request) {
        memberValidator.validateAndGetMember(memberId);

        return foodTruckInfoService.getFoodTrucks(memberId, request);
    }

    public FoodTruckNameDuplicateCheckResponse isNameDuplicated(Long ownerId, FoodTruckNameDuplicateCheckRequest request) {
        ownerValidator.validateAndGetOwner(ownerId);

        return FoodTruckNameDuplicateCheckResponse.of(
                foodTruckInfoService.isNameDuplicated(request.name()));
    }

    public CursorPagingResponse<FoodTruckMenuResponse> getFoodTruckMenus(Long memberId, Long foodTruckId, FoodTruckMenuRequest request) {
        memberValidator.validateAndGetMember(memberId);

        return foodTruckMenuService.getFoodTruckMenus(foodTruckId, request);
    }

    public ImageResponse createFoodTruckImagePresignedUrl(ImageRequest request, Long ownerId) {
        User owner = ownerValidator.validateAndGetOwner(ownerId);

        return foodTruckImageService.createFoodTruckImagePresignedUrl(request, owner);
    }

    public ImageResponse createMenuImagePresignedUrl(ImageRequest request, Long ownerId) {
        User owner = ownerValidator.validateAndGetOwner(ownerId);

        return foodTruckImageService.createMenuImagePresignedUrl(request, owner);
    }

    @Transactional
    public FoodTruckIdResponse updateMyFoodTruckInfo(Long ownerId, Long foodTruckId, UpdateFoodTruckInfoRequest request) {
        User owner = ownerValidator.validateAndGetOwner(ownerId);

        return FoodTruckIdResponse.of(
                foodTruckInfoService.updateMyFoodTruckInfo(owner, foodTruckId, request)
        );
    }

    public void deleteFoodTruckImagesFromS3(Long ownerId, Long foodTruckId, DeleteFoodTruckImagesRequest request) {
        User owner = ownerValidator.validateAndGetOwner(ownerId);

        foodTruckImageService.deleteFoodTruckImagesFromS3(owner, foodTruckId, request);
    }
}
