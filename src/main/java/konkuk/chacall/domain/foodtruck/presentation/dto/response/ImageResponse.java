package konkuk.chacall.domain.foodtruck.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ImageResponse(
        @Schema(description = "생성된 presigned URL 리스트", example = "[\"https://example.com/presigned-url1\", \"https://example.com/presigned-url2\"]")
        List<ImageInfo> presignedUrls
) {
    public record ImageInfo(
            @Schema(description = "생성된 presigned URL", example = "https://example.com/presigned-url")
            String presignedUrl,

            @Schema(description = "파일 접근 URL", example = "https://example.com/file-url")
            String fileUrl
    ) {
        public static ImageInfo of(String presignedUrl, String fileUrl) {
            return new ImageInfo(presignedUrl, fileUrl);
        }
    }

    public static ImageResponse of(List<ImageInfo> imageInfos) {
        return new ImageResponse(imageInfos);
    }
}
