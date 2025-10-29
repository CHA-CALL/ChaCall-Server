package konkuk.chacall.domain.foodtruck.application.image;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.DeleteFoodTruckImagesRequest;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import konkuk.chacall.global.common.storage.S3Service;
import konkuk.chacall.global.common.storage.dto.ImageRequest;
import konkuk.chacall.global.common.storage.dto.ImageResponse;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.storage.presign.PresignedUrlService;
import konkuk.chacall.global.common.storage.util.CdnUrlResolver;
import konkuk.chacall.global.common.storage.util.KeyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodTruckImageService {

    private final PresignedUrlService presignedUrlService;
    private static final int MAX_FOOD_TRUCK_IMAGE_COUNT = 9;

    private final FoodTruckRepository foodTruckRepository;
    private final S3Service s3Service;
    private final CdnUrlResolver cdnUrlResolver;

    public ImageResponse createFoodTruckImagePresignedUrl(ImageRequest request, User owner) {
        return presignedUrlService.generatePresignedUrls(
                request,
                owner.getUserId(),
                MAX_FOOD_TRUCK_IMAGE_COUNT,
                KeyUtils::buildFoodTruckImageKey
        );
    }

    public ImageResponse createMenuImagePresignedUrl(ImageRequest request, User owner) {
        return presignedUrlService.generatePresignedUrls(
                request,
                owner.getUserId(),
                null,
                KeyUtils::buildMenuImageKey
        );
    }

    public void deleteFoodTruckImagesFromS3(User owner, Long foodTruckId, DeleteFoodTruckImagesRequest request) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_TRUCK_NOT_FOUND));

        foodTruck.validateOwner(owner.getUserId());

        request.imageUrls().stream()
                .map(cdnUrlResolver::extractKeyFromUrl)
                .forEach(s3Service::delete);
    }
}