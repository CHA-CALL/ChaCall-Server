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

        if(fileExtensions.size() > MAX_FOOD_TRUCK_IMAGE_COUNT) {
            throw new BusinessException(ErrorCode.INVALID_IMAGE_COUNT,
                    new IllegalArgumentException("푸드트럭 이미지는 1장 이상 " + MAX_FOOD_TRUCK_IMAGE_COUNT + "장 이하로 등록해야 합니다. 입력된 이미지 개수: " + fileExtensions.size()));
        }

        AllowedFileExtension.checkAllowedExtension(fileExtensions);

        var imageInfos = fileExtensions.stream()
                .map(extension -> {
                    String baseKey = KeyUtils.buildFoodTruckImageKey(owner.getUserId());
                    String keyWithExt = baseKey + "." + extension.toLowerCase();

                    String presignedUrl = s3Service.generatePresignedUrl(keyWithExt);
                    String fileUrl = s3Service.getFileUrl(keyWithExt);

                    return ImageResponse.ImageInfo.of(presignedUrl, fileUrl);
                })
                .toList();

        return ImageResponse.of(imageInfos);
    }

    public ImageResponse createMenuImagePresignedUrl(ImageRequest request, User owner) {
        List<String> fileExtensions = request.fileExtensions();

        AllowedFileExtension.checkAllowedExtension(fileExtensions);

        var imageInfos = fileExtensions.stream()
                .map(extension -> {
                    String baseKey = KeyUtils.buildMenuImageKey(owner.getUserId());
                    String keyWithExt = baseKey + "." + extension.toLowerCase();

                    String presignedUrl = s3Service.generatePresignedUrl(keyWithExt);
                    String fileUrl = s3Service.getFileUrl(keyWithExt);

                    return ImageResponse.ImageInfo.of(presignedUrl, fileUrl);
                })
                .toList();

        return ImageResponse.of(imageInfos);
    }
}