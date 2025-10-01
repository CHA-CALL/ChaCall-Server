package konkuk.chacall.domain.foodtruck.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ImageResponse(
        @Schema(description = "생성된 presigned URL 리스트", example = "[\"https://example.com/presigned-url1\", \"https://example.com/presigned-url2\"]")
        List<String> presignedUrls
) {
    public static ImageResponse of(List<String> presignedUrls) {
        return new ImageResponse(presignedUrls);
    }
}
