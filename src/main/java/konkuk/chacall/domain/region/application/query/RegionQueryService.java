package konkuk.chacall.domain.region.application.query;

import konkuk.chacall.domain.region.domain.model.Region;
import konkuk.chacall.domain.region.domain.repository.RegionRepository;
import konkuk.chacall.domain.region.presentation.dto.request.RegionQueryRequest;
import konkuk.chacall.domain.region.presentation.dto.response.RegionQueryResponse;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RegionQueryService {

    private final RegionRepository regionRepository;

    public List<RegionQueryResponse> getRegions(RegionQueryRequest request) {
        if (request.depth() >= 2 && !regionRepository.existsByRegionCode(request.parentCode())) {
            throw new EntityNotFoundException(ErrorCode.REGION_PARENT_NOT_FOUND);
        }

        List<Region> regions = regionRepository.findRegions(request.depth(), request.parentCode());

        return regions.stream()
                .map(RegionQueryResponse::of)
                .toList();
    }
}
