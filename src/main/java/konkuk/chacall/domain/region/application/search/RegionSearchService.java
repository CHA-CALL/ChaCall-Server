package konkuk.chacall.domain.region.application.search;

import konkuk.chacall.domain.region.domain.model.Region;
import konkuk.chacall.domain.region.domain.repository.RegionRepository;
import konkuk.chacall.domain.region.presentation.dto.request.RegionSearchRequest;
import konkuk.chacall.domain.region.presentation.dto.response.RegionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RegionSearchService {

    private final RegionRepository regionRepository;

    public List<RegionResponse> searchRegions(RegionSearchRequest request) {
        List<Region> regions = regionRepository.findByFullNameContainingOrderByRegionIdAsc(request.keyword().trim());

        return regions.stream()
                .map(region -> RegionResponse.of(region.getFullName(), region.getRegionCode()))
                .toList();
    }

}
