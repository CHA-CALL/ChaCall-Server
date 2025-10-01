package konkuk.chacall.domain.foodtruck.presentation.dto.response;

import java.util.List;

public record ImageResponse(
        List<String> presignedUrls
) {
    public static ImageResponse of(List<String> presignedUrls) {
        return new ImageResponse(presignedUrls);
    }
}
