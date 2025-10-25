package konkuk.chacall.domain.foodtruck.application.image;

import konkuk.chacall.global.common.storage.dto.ImageRequest;
import konkuk.chacall.global.common.storage.dto.ImageResponse;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.storage.presign.PresignedUrlService;
import konkuk.chacall.global.common.storage.util.KeyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodTruckImageService {

    private final PresignedUrlService presignedUrlService;
    private static final int MAX_FOOD_TRUCK_IMAGE_COUNT = 9;

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
}