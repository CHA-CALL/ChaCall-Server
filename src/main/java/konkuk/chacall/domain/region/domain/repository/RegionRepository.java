package konkuk.chacall.domain.region.domain.repository;

import konkuk.chacall.domain.region.domain.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query("""
                select r
                from Region r
                where r.depth = :depth
                  and (:parentCode is null or r.parentCode = :parentCode)
                order by r.regionId asc
            """)
    List<Region> findRegions(Integer depth, Long parentCode);

    boolean existsByRegionCode(Long regionCode);
}
