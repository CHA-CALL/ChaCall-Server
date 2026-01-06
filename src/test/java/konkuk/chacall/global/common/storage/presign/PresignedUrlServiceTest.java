package konkuk.chacall.global.common.storage.presign;

import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import konkuk.chacall.global.common.storage.S3Service;
import konkuk.chacall.global.common.storage.dto.ImageRequest;
import konkuk.chacall.global.common.storage.dto.ImageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("PresignedUrlService 테스트")
class PresignedUrlServiceTest {

    @InjectMocks
    private PresignedUrlService presignedUrlService;

    @Mock
    private S3Service s3Service;

    private final Function<Long, String> sampleKeyBuilder = userId -> "user-" + userId;
    private final Long SAMPLE_USER_ID = 1L;

    @Nested
    @DisplayName("Pre-signed URL 생성 시나리오")
    class GeneratePresignedUrlsScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상적인 확장자와 개수 요청 시 Pre-signed URL과 파일 URL을 성공적으로 생성한다")
            void generateUrlsSuccess() {
                // given
                ImageRequest request = new ImageRequest(List.of("jpg", "png"));
                given(s3Service.generatePresignedUrl(anyString())).willReturn("presigned_url");
                given(s3Service.getFileUrl(anyString())).willReturn("file_url");

                // when
                ImageResponse response = presignedUrlService.generatePresignedUrls(request, SAMPLE_USER_ID, 5, sampleKeyBuilder);

                // then
                assertThat(response.presignedUrls()).hasSize(2);
                assertThat(response.presignedUrls().get(0).presignedUrl()).isEqualTo("presigned_url");
                assertThat(response.presignedUrls().get(0).fileUrl()).isEqualTo("file_url");
                verify(s3Service, times(2)).generatePresignedUrl(anyString());
                verify(s3Service, times(2)).getFileUrl(anyString());
            }

            @Test
            @DisplayName("maxCount가 null일 때도 정상적으로 생성한다")
            void generateUrlsWithNullMaxCountSuccess() {
                // given
                ImageRequest request = new ImageRequest(List.of("jpeg"));
                given(s3Service.generatePresignedUrl(anyString())).willReturn("presigned_url");
                given(s3Service.getFileUrl(anyString())).willReturn("file_url");

                // when
                ImageResponse response = presignedUrlService.generatePresignedUrls(request, SAMPLE_USER_ID, null, sampleKeyBuilder);

                // then
                assertThat(response.presignedUrls()).hasSize(1);
                verify(s3Service).generatePresignedUrl(anyString());
                verify(s3Service).getFileUrl(anyString());
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("최대 허용 개수를 초과하여 요청 시 INVALID_IMAGE_COUNT 예외를 발생시킨다")
            void generateUrlsWithExceededCountFail() {
                // given
                ImageRequest request = new ImageRequest(List.of("jpg", "png", "gif"));
                int maxCount = 2;

                // when
                BusinessException ex = assertThrows(BusinessException.class,
                        () -> presignedUrlService.generatePresignedUrls(request, SAMPLE_USER_ID, maxCount, sampleKeyBuilder));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.INVALID_IMAGE_COUNT);
            }

            @Test
            @DisplayName("허용되지 않은 확장자로 요청 시 INVALID_FILE_EXTENSION 예외를 발생시킨다")
            void generateUrlsWithInvalidExtensionFail() {
                // given
                ImageRequest request = new ImageRequest(List.of("jpg", "exe"));

                // when
                BusinessException ex = assertThrows(BusinessException.class,
                        () -> presignedUrlService.generatePresignedUrls(request, SAMPLE_USER_ID, 5, sampleKeyBuilder));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.INVALID_FILE_EXTENSION);
            }
        }
    }
}
