package konkuk.chacall.domain.foodtruck.domain.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.value.AvailableQuantity;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckStatus;
import konkuk.chacall.domain.foodtruck.domain.value.MenuCategory;
import konkuk.chacall.domain.foodtruck.domain.value.PaymentMethod;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.DateRangeRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.FoodTruckSearchRequest;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static konkuk.chacall.domain.foodtruck.domain.model.QAvailableDate.availableDate;
import static konkuk.chacall.domain.foodtruck.domain.model.QFoodTruck.foodTruck;
import static konkuk.chacall.domain.foodtruck.domain.model.QFoodTruckServiceArea.foodTruckServiceArea;
import static konkuk.chacall.domain.region.domain.model.QRegion.region;

@Repository
public class FoodTruckSearchRepositoryImpl implements FoodTruckSearchRepository{

    private final JPAQueryFactory query;

    public FoodTruckSearchRepositoryImpl(JPAQueryFactory query) {
        this.query = query;
    }

    @Override
    public Slice<FoodTruck> getFoodTrucks(FoodTruckSearchRequest request) {
        BooleanBuilder where = new BooleanBuilder();

        // 검색어
        if(request.keyword() != null && !request.keyword().isBlank()) {
            String keyword = "%" + request.keyword().toLowerCase()+ "%";
            where.and(foodTruck.name.lower().like(keyword)
                    .or(foodTruck.description.lower().like(keyword)));
        }

        // 지역 - prefix
        if(request.regionCode() != null) {
            BooleanExpression regionMatch = JPAExpressions.selectOne()
                    .from(foodTruckServiceArea)
                    .join(foodTruckServiceArea.region, region)
                    .where(foodTruckServiceArea.foodTruck.foodTruckId.eq(foodTruck.foodTruckId),
                            region.regionCode.stringValue().startsWith(request.regionCode().toString()))
                    .exists();

            where.and(regionMatch);
        }

        // 일정
        if (request.schedules() != null && !request.schedules().isEmpty()) {
            BooleanBuilder any = new BooleanBuilder();

            for (DateRangeRequest dateRange : request.schedules()) {
                BooleanExpression covered = JPAExpressions.selectOne()
                        .from(availableDate)
                        .where(availableDate.foodTruck.foodTruckId.eq(foodTruck.foodTruckId),
                                availableDate.startAt.loe(dateRange.startDate()),
                                availableDate.endAt.goe(dateRange.endDate()))
                        .exists();
                any.or(covered);
            }

            where.and(any);
        }

        // 수량
        if (request.availableQuantity() != null) {
            where.and(foodTruck.availableQuantity.in(
                    AvailableQuantity.acceptableFor(request.availableQuantity())
            ));
        }

        // 메뉴 카테고리
        if (request.categories() != null && !request.categories().isEmpty()) {
            var any = new BooleanBuilder();
            for (MenuCategory category : request.categories()) {
                any.or(Expressions.booleanTemplate(
                        "concat(',', {0}, ',') like concat('%,', {1}, ',%')",
                        foodTruck.menuCategoryList,
                        Expressions.constant(category.getValue())
                ));
            }
            where.and(any);
        }

        // 전기
        if(request.needElectricity() != null) {
            where.and(foodTruck.needElectricity.eq(request.needElectricity()));
        }

        // 결제방법
        if (request.paymentMethod() != null && request.paymentMethod() != PaymentMethod.ANY) {
            where.and(foodTruck.paymentMethod.eq(request.paymentMethod()));
        }

        // 푸드트럭 상태
        where.and(foodTruck.foodTruckStatus.eq(FoodTruckStatus.ON));

        // 커서 기반 페이징
        var paging = request.pagingOrDefault();
        Long lastCursor = paging.cursor();
        int size = paging.size();

        // 커서 기반 페이징
        where.and(foodTruck.foodTruckId.lt(lastCursor));

        List<FoodTruck> content = query.selectFrom(foodTruck)
                .where(where)
                .orderBy(foodTruck.foodTruckId.desc())
                .limit(size + 1)
                .fetch();

        boolean hasNext = content.size() > size;
        if(hasNext) content.remove(size);

        return new SliceImpl<>(content, PageRequest.of(0, size), hasNext);
    }
}
