package konkuk.chacall.domain.region.application.query;

import konkuk.chacall.domain.region.domain.model.Region;
import konkuk.chacall.domain.region.domain.repository.RegionRepository;
import konkuk.chacall.domain.region.presentation.dto.request.RegionQueryRequest;
import konkuk.chacall.domain.region.presentation.dto.response.RegionResponse;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RegionQueryService {

    private final RegionRepository regionRepository;

    public List<RegionResponse> getRegions(RegionQueryRequest request) {
        if (request.depth() >= 2 && !regionRepository.existsByRegionCodeAndDepth(request.parentCode(), request.depth() - 1)) {
            throw new EntityNotFoundException(ErrorCode.PARENT_REGION_NOT_FOUND);
        }

        List<Region> regions = regionRepository.findRegions(request.depth(), request.parentCode());

        return regions.stream()
                .map(region -> RegionResponse.of(region.getName(), region.getRegionCode()))
                .toList();
    }
}
