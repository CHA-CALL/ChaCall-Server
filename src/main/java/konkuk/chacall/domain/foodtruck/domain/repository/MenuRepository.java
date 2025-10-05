package konkuk.chacall.domain.foodtruck.domain.repository;

import konkuk.chacall.domain.foodtruck.domain.model.Menu;
import konkuk.chacall.domain.foodtruck.domain.value.MenuViewedStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Modifying
    @Query("DELETE FROM Menu m WHERE m.foodTruck.foodTruckId = :foodTruckId")
    void deleteAllByFoodTruckId(@Param("foodTruckId") Long foodTruckId);

    @Query("""
            select m
            from Menu m
            where m.foodTruck.foodTruckId = :foodTruckId
              and m.menuId < :lastCursor
            order by m.menuId desc
            """)
    Slice<Menu> findMenusDesc(@Param("foodTruckId") Long foodTruckId,
                              @Param("lastCursor") Long lastCursor,
                              Pageable pageable);

    @Query("""
            select m
            from Menu m
            where m.foodTruck.foodTruckId = :foodTruckId
              and m.menuId > :lastCursor
            order by m.menuId asc
            """)
    Slice<Menu> findMenusAsc(@Param("foodTruckId") Long foodTruckId,
                             @Param("lastCursor") Long lastCursor,
                             Pageable pageable);

    @Query("""
            select m
            from Menu m
            where m.foodTruck.foodTruckId = :foodTruckId
              and m.menuViewedStatus = konkuk.chacall.domain.foodtruck.domain.value.MenuViewedStatus.ON
              and m.menuId < :lastCursor
            order by m.menuId desc
            """)
    Slice<Menu> findVisibleMenusDesc(@Param("foodTruckId") Long foodTruckId,
                                     @Param("lastCursor") Long lastCursor,
                                     Pageable pageable);

    @Query("""
            select m
            from Menu m
            where m.foodTruck.foodTruckId = :foodTruckId
              and m.menuViewedStatus = konkuk.chacall.domain.foodtruck.domain.value.MenuViewedStatus.ON
              and m.menuId > :lastCursor
            order by m.menuId asc
            """)
    Slice<Menu> findVisibleMenusAsc(@Param("foodTruckId") Long foodTruckId,
                                                 @Param("lastCursor") Long lastCursor,
                                                 Pageable pageable);

    @Query("""
                select m from Menu m
                where m.menuId = :menuId
                  and m.foodTruck.foodTruckId = :foodTruckId
            """)
    Optional<Menu> findByMenuIdAndFoodTruckId(@Param("menuId") Long menuId,
                                              @Param("foodTruckId") Long foodTruckId);
}
