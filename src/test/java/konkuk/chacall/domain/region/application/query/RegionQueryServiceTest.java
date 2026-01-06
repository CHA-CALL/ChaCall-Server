package konkuk.chacall.domain.region.application.query;

import konkuk.chacall.domain.region.domain.model.Region;
import konkuk.chacall.domain.region.domain.repository.RegionRepository;
import konkuk.chacall.domain.region.presentation.dto.request.RegionQueryRequest;
import konkuk.chacall.domain.region.presentation.dto.response.RegionResponse;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegionQueryService 테스트")
class RegionQueryServiceTest {

    @InjectMocks
    private RegionQueryService regionQueryService;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private Region region;

    @Nested
    @DisplayName("지역 목록 조회 시나리오")
    class GetRegionsScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("depth 1 (시/도) 조회 시 상위 지역 검증 없이 목록을 반환한다")
            void getDepth1RegionsSuccess() {
                // given
                RegionQueryRequest request = new RegionQueryRequest(1, null);
                given(regionRepository.findRegions(anyInt(), isNull())).willReturn(Collections.singletonList(region));

                // when
                List<RegionResponse> responses = regionQueryService.getRegions(request);

                // then
                assertThat(responses).hasSize(1);
                verify(regionRepository, never()).existsByRegionCodeAndDepth(any(), anyInt());
                verify(regionRepository).findRegions(eq(1), isNull());
            }

            @Test
            @DisplayName("depth 2 (시/군/구) 조회 시 상위 지역이 존재하면 목록을 반환한다")
            void getDepth2RegionsSuccess() {
                // given
                RegionQueryRequest request = new RegionQueryRequest(2, 11L);
                given(regionRepository.existsByRegionCodeAndDepth(anyLong(), anyInt())).willReturn(true);
                given(regionRepository.findRegions(anyInt(), anyLong())).willReturn(Collections.singletonList(region));

                // when
                List<RegionResponse> responses = regionQueryService.getRegions(request);

                // then
                assertThat(responses).hasSize(1);
                verify(regionRepository).existsByRegionCodeAndDepth(eq(11L), eq(1));
                verify(regionRepository).findRegions(eq(2), eq(11L));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("depth 2 이상 조회 시 상위 지역 코드가 존재하지 않으면 PARENT_REGION_NOT_FOUND 예외를 발생시킨다")
            void getRegionsWithInvalidParentCodeFail() {
                // given
                RegionQueryRequest request = new RegionQueryRequest(2, 99L);
                given(regionRepository.existsByRegionCodeAndDepth(anyLong(), anyInt())).willReturn(false);

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> regionQueryService.getRegions(request));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.PARENT_REGION_NOT_FOUND);
                verify(regionRepository).existsByRegionCodeAndDepth(eq(99L), eq(1));
                verify(regionRepository, never()).findRegions(anyInt(), anyLong());
            }
        }
    }
}
