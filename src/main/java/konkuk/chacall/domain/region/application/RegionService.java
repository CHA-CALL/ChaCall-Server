package konkuk.chacall.domain.region.application;

import konkuk.chacall.domain.region.application.query.RegionQueryService;
import konkuk.chacall.domain.region.presentation.dto.request.RegionQueryRequest;
import konkuk.chacall.domain.region.presentation.dto.response.RegionQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {

    private final RegionQueryService regionQueryService;

    public List<RegionQueryResponse> getRegions(RegionQueryRequest request) {
        return regionQueryService.getRegions(request);
    }
}
