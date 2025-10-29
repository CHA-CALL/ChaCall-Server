package konkuk.chacall.global.common.storage.presign;

import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import konkuk.chacall.global.common.storage.S3Service;
import konkuk.chacall.global.common.storage.dto.ImageRequest;
import konkuk.chacall.global.common.storage.dto.ImageResponse;
import konkuk.chacall.global.common.storage.util.AllowedFileExtension;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class PresignedUrlService {

    private final S3Service s3Service;

    public ImageResponse generatePresignedUrls(
            ImageRequest request,
            Long userId,
            Integer maxCountOrNull,
            Function<Long, String> baseKeyBuilder
    ) {
        final List<String> extensions = request.fileExtensions();

        // 1) 개수 검증
        if (maxCountOrNull != null && extensions.size() > maxCountOrNull) {
            throw new BusinessException(
                    ErrorCode.INVALID_IMAGE_COUNT,
                    new IllegalArgumentException("이미지 개수는 1 이상 " + maxCountOrNull + " 이하이어야 합니다. 입력: " + extensions.size())
            );
        }

        // 2) 확장자 검증
        AllowedFileExtension.checkAllowedExtension(extensions);

        // 3) 키 조합 및 Presigned URL 생성
        final String baseKey = baseKeyBuilder.apply(userId);

        var imageInfos = extensions.stream()
                .map(extension -> {
                    final String keyWithExt = baseKey + "." + extension.toLowerCase();

                    final String presignedUrl = s3Service.generatePresignedUrl(keyWithExt);
                    final String fileUrl = s3Service.getFileUrl(keyWithExt);

                    return ImageResponse.ImageInfo.of(presignedUrl, fileUrl);
                })
                .toList();

        return ImageResponse.of(imageInfos);
    }
}