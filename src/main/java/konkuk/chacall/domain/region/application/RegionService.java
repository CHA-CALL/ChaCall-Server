package konkuk.chacall.domain.region.application;

import konkuk.chacall.domain.region.application.query.RegionQueryService;
import konkuk.chacall.domain.region.application.search.RegionSearchService;
import konkuk.chacall.domain.region.presentation.dto.request.RegionQueryRequest;
import konkuk.chacall.domain.region.presentation.dto.request.RegionSearchRequest;
import konkuk.chacall.domain.region.presentation.dto.response.RegionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionQueryService regionQueryService;
    private final RegionSearchService regionSearchService;

    @Transactional(readOnly = true)
    public List<RegionResponse> getRegions(RegionQueryRequest request) {
        return regionQueryService.getRegions(request);
    }

    @Transactional(readOnly = true)
    public List<RegionResponse> searchRegions(RegionSearchRequest request) {
        return regionSearchService.searchRegions(request);
    }
}
