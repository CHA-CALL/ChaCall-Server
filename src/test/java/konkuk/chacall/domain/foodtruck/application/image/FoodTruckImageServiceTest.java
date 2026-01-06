package konkuk.chacall.domain.foodtruck.application.image;

import konkuk.chacall.domain.foodtruck.application.image.FoodTruckImageService;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.DeleteFoodTruckImagesRequest;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.AuthException;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import konkuk.chacall.global.common.storage.S3Service;
import konkuk.chacall.global.common.storage.dto.ImageRequest;
import konkuk.chacall.global.common.storage.dto.ImageResponse;
import konkuk.chacall.global.common.storage.presign.PresignedUrlService;
import konkuk.chacall.global.common.storage.util.CdnUrlResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FoodTruckImageService 테스트")
class FoodTruckImageServiceTest {

    @InjectMocks
    private FoodTruckImageService foodTruckImageService;

    @Mock
    private PresignedUrlService presignedUrlService;

    @Mock
    private FoodTruckRepository foodTruckRepository;

    @Mock
    private S3Service s3Service;

    @Mock
    private CdnUrlResolver cdnUrlResolver;

    @Mock
    private User owner;

    @Mock
    private FoodTruck foodTruck;

    @Nested
    @DisplayName("푸드트럭 이미지 Pre-signed URL 생성 시나리오")
    class CreateFoodTruckImagePresignedUrlScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상 요청 시 Pre-signed URL 생성 로직을 호출하고 결과를 반환한다")
            void createFoodTruckImagePresignedUrlSuccess() {
                // given
                ImageRequest request = new ImageRequest(Collections.emptyList());
                ImageResponse expectedResponse = new ImageResponse(Collections.emptyList());
                given(owner.getUserId()).willReturn(1L);
                given(presignedUrlService.generatePresignedUrls(any(ImageRequest.class), anyLong(), anyInt(), any(Function.class)))
                        .willReturn(expectedResponse);

                // when
                ImageResponse actualResponse = foodTruckImageService.createFoodTruckImagePresignedUrl(request, owner);

                // then
                assertThat(actualResponse).isEqualTo(expectedResponse);
                verify(presignedUrlService).generatePresignedUrls(any(ImageRequest.class), anyLong(), eq(9), any(Function.class));
            }
        }
    }

    @Nested
    @DisplayName("메뉴 이미지 Pre-signed URL 생성 시나리오")
    class CreateMenuImagePresignedUrlScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상 요청 시 Pre-signed URL 생성 로직을 호출하고 결과를 반환한다")
            void createMenuImagePresignedUrlSuccess() {
                // given
                ImageRequest request = new ImageRequest(Collections.emptyList());
                ImageResponse expectedResponse = new ImageResponse(Collections.emptyList());
                given(owner.getUserId()).willReturn(1L);
                given(presignedUrlService.generatePresignedUrls(any(ImageRequest.class), anyLong(), isNull(), any(Function.class)))
                        .willReturn(expectedResponse);

                // when
                ImageResponse actualResponse = foodTruckImageService.createMenuImagePresignedUrl(request, owner);

                // then
                assertThat(actualResponse).isEqualTo(expectedResponse);
                verify(presignedUrlService).generatePresignedUrls(any(ImageRequest.class), anyLong(), isNull(), any(Function.class));
            }
        }
    }

    @Nested
    @DisplayName("푸드트럭 이미지 S3에서 삭제 시나리오")
    class DeleteFoodTruckImagesFromS3Scenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상 요청 시 S3에서 해당 이미지들을 삭제한다")
            void deleteImagesSuccess() {
                // given
                DeleteFoodTruckImagesRequest request = new DeleteFoodTruckImagesRequest(List.of("cdn_url_1", "cdn_url_2"));
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                given(owner.getUserId()).willReturn(1L);
                doNothing().when(foodTruck).validateOwner(anyLong());
                given(cdnUrlResolver.extractKeyFromUrl(anyString())).willReturn("s3_key");
                doNothing().when(s3Service).delete(anyString());

                // when
                foodTruckImageService.deleteFoodTruckImagesFromS3(owner, 1L, request);

                // then
                verify(foodTruckRepository).findById(anyLong());
                verify(foodTruck).validateOwner(anyLong());
                verify(cdnUrlResolver, times(2)).extractKeyFromUrl(anyString());
                verify(s3Service, times(2)).delete(anyString());
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 푸드트럭 ID로 요청 시 FOOD_TRUCK_NOT_FOUND 예외를 발생시킨다")
            void deleteImagesWithInvalidFoodTruckIdFail() {
                // given
                DeleteFoodTruckImagesRequest request = new DeleteFoodTruckImagesRequest(Collections.emptyList());
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.empty());

                // when
                BusinessException ex = assertThrows(BusinessException.class,
                        () -> foodTruckImageService.deleteFoodTruckImagesFromS3(owner, 1L, request));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FOOD_TRUCK_NOT_FOUND);
            }

            @Test
            @DisplayName("소유주가 아닌 사용자가 요청 시 AUTH_FORBIDDEN 예외를 발생시킨다")
            void deleteImagesByNotOwnerFail() {
                // given
                DeleteFoodTruckImagesRequest request = new DeleteFoodTruckImagesRequest(Collections.emptyList());
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                given(owner.getUserId()).willReturn(1L);
                doThrow(new AuthException(ErrorCode.USER_FORBIDDEN)).when(foodTruck).validateOwner(anyLong());

                // when
                AuthException ex = assertThrows(AuthException.class,
                        () -> foodTruckImageService.deleteFoodTruckImagesFromS3(owner, 1L, request));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_FORBIDDEN);
            }
        }
    }
}
