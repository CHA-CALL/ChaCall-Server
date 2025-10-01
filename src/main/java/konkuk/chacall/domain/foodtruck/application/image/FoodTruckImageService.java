package konkuk.chacall.domain.foodtruck.application.image;

import konkuk.chacall.domain.foodtruck.presentation.dto.request.ImageRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.ImageResponse;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import konkuk.chacall.global.common.storage.S3Service;
import konkuk.chacall.global.common.storage.util.AllowedFileExtension;
import konkuk.chacall.global.common.storage.util.KeyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodTruckImageService {

    private final S3Service s3Service;
    private static final int MAX_FOOD_TRUCK_IMAGE_COUNT = 9;
//    private static final int MAX_MENU_IMAGE_COUNT = 5;

    public ImageResponse createFoodTruckImagePresignedUrl(ImageRequest request, User owner) {
        List<String> fileExtensions = request.fileExtensions();

        if(fileExtensions == null || fileExtensions.isEmpty() || fileExtensions.size() > MAX_FOOD_TRUCK_IMAGE_COUNT) {
            throw new BusinessException(ErrorCode.INVALID_IMAGE_COUNT,
                    new IllegalArgumentException("푸드트럭 이미지는 1장 이상 " + MAX_FOOD_TRUCK_IMAGE_COUNT + "장 이하로 등록해야 합니다. 입력된 이미지 개수: " + (fileExtensions == null ? 0 : fileExtensions.size())));
        }

        AllowedFileExtension.checkAllowedExtension(fileExtensions);

        List<String> presignedUrls = fileExtensions.stream()
                .map(extension -> KeyUtils.buildFoodTruckImageKey(owner.getUserId()))
                .map(s3Service::generatePresignedUrl)
                .toList();

        return ImageResponse.of(presignedUrls);
    }

    public ImageResponse createMenuImagePresignedUrl(ImageRequest request, User owner) {
        List<String> fileExtensions = request.fileExtensions();

        if(fileExtensions == null || fileExtensions.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_IMAGE_COUNT,
                    new IllegalArgumentException("메뉴 이미지는 1장 이상 " + "장 이하로 등록해야 합니다. 입력된 이미지 개수: " + (fileExtensions == null ? 0 : fileExtensions.size())));
        }

        AllowedFileExtension.checkAllowedExtension(fileExtensions);

        List<String> presignedUrls = fileExtensions.stream()
                .map(extension -> KeyUtils.buildMenuImageKey(owner.getUserId()))
                .map(s3Service::generatePresignedUrl)
                .toList();

        return ImageResponse.of(presignedUrls);
    }
}
