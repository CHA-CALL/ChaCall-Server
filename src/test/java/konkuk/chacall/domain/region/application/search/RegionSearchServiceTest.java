package konkuk.chacall.domain.region.application.search;

import konkuk.chacall.domain.region.domain.model.Region;
import konkuk.chacall.domain.region.domain.repository.RegionRepository;
import konkuk.chacall.domain.region.presentation.dto.request.RegionSearchRequest;
import konkuk.chacall.domain.region.presentation.dto.response.RegionResponse;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegionSearchService 테스트")
class RegionSearchServiceTest {

    @InjectMocks
    private RegionSearchService regionSearchService;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private Region region;

    @Nested
    @DisplayName("지역 검색 시나리오")
    class SearchRegionsScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("키워드로 지역 검색 시 일치하는 지역 목록을 반환한다")
            void searchRegionsByKeywordSuccess() {
                // given
                RegionSearchRequest request = new RegionSearchRequest("  광진구  ");
                String trimmedKeyword = "광진구";
                given(regionRepository.searchSubRegionsByFullName(anyString()))
                        .willReturn(Collections.singletonList(region));
                given(region.getFullName()).willReturn("서울특별시 광진구");
                given(region.getRegionId()).willReturn(1L);
                given(region.getRegionCode()).willReturn(11215L);

                // when
                List<RegionResponse> responses = regionSearchService.searchRegions(request);

                // then
                assertThat(responses).hasSize(1);
                assertThat(responses.get(0).name()).isEqualTo("서울특별시 광진구");
                verify(regionRepository).searchSubRegionsByFullName(trimmedKeyword);
            }

            @Test
            @DisplayName("검색 결과가 없을 경우 빈 목록을 반환한다")
            void searchRegionsReturnsEmptyListWhenNoMatch() {
                // given
                RegionSearchRequest request = new RegionSearchRequest("없는 지역");
                given(regionRepository.searchSubRegionsByFullName(anyString()))
                        .willReturn(Collections.emptyList());

                // when
                List<RegionResponse> responses = regionSearchService.searchRegions(request);

                // then
                assertThat(responses).isEmpty();
                verify(regionRepository).searchSubRegionsByFullName("없는 지역");
            }
        }
    }
}
