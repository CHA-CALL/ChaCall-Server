package konkuk.chacall.domain.reservation.domain.model;

import jakarta.persistence.*;
import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import konkuk.chacall.domain.reservation.domain.value.ReservationDateList;
import konkuk.chacall.domain.reservation.domain.value.ReservationInfo;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.domain.BaseEntity;
import konkuk.chacall.global.common.exception.DomainRuleException;
import lombok.*;

import java.util.List;

import static konkuk.chacall.global.common.exception.code.ErrorCode.*;

@Getter
@Entity
@Table(name = "reservations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus reservationStatus;

    @Column(length = 500)
    private String pdfUrl; // 견적서 PDF URL

    @Embedded
    private ReservationInfo reservationInfo; // 예약 정보

    // '예약자(손님)'와의 연관관계 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User member;

    // '예약된 푸드트럭'과의 연관관계 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_truck_id", nullable = false)
    private FoodTruck foodTruck;

    // 해당 예약과 연관된 사람인지 검증 (사장님, 예약자)
    public void validateAccessibleBy(Long userId) {
        if (!isForFoodTruckOwnedBy(userId) && !isReservedBy(userId)) {
            throw new DomainRuleException(RESERVATION_NOT_OWNED);
        }
    }

    // 본인이 푸드트럭 소유자인지 검증
    public void validateFoodTruckOwner(Long ownerId) {
        if (!isForFoodTruckOwnedBy(ownerId)) {
            throw new DomainRuleException(RESERVATION_NOT_OWNED);
        }
    }

    private boolean isForFoodTruckOwnedBy(Long ownerId) {
        return this.foodTruck.getOwner().getUserId().equals(ownerId);
    }

    // 본인이 예약자인지 검증
    public void validateReservedBy(Long memberId) {
        if (!isReservedBy(memberId)) {
            throw new DomainRuleException(RESERVATION_NOT_OWNED);
        }
    }

    private boolean isReservedBy(Long memberId) {
        return this.member.getUserId().equals(memberId);
    }

    public void validateCanBeRatedBy(User member) {
        if (!this.member.getUserId().equals(member.getUserId())) {
            throw new DomainRuleException(CANNOT_RATE_RESERVATION_NOT_OWNED);
        }
        if (this.reservationStatus != ReservationStatus.CONFIRMED) {
            throw new DomainRuleException(CANNOT_RATE_UNCONFIRMED_RESERVATION);
        }
    }

    public static Reservation create(
            String reservationAddress,
            String reservationDetailAddress,
            List<String> reservationDateStrings,
            String operationHour,
            String menu,
            Integer reservationDeposit,
            boolean isUseElectricity,
            String etcRequest,
            User owner,
            User member,
            FoodTruck foodTruck
    ) {
        validateCreateReservation(owner, member, foodTruck);

        ReservationInfo reservationInfo = ReservationInfo.builder()
                .address(reservationAddress)
                .detailAddress(reservationDetailAddress)
                .reservationDates(ReservationDateList.fromJson(reservationDateStrings))
                .operationHour(operationHour)
                .menu(menu)
                .deposit(reservationDeposit)
                .isUseElectricity(isUseElectricity)
                .etcRequest(etcRequest)
                .build();

        return Reservation.builder()
                .reservationId(null)
                .reservationStatus(ReservationStatus.PENDING) // 기본 상태: 예약 대기
                .reservationInfo(reservationInfo)
                .pdfUrl(null)
                .member(member)
                .foodTruck(foodTruck)
                .build();
    }

    private static void validateCreateReservation(User owner, User member, FoodTruck foodTruck) {
        foodTruck.validateOwner(owner.getUserId());
        if (foodTruck.getOwner().getUserId().equals(member.getUserId())) {
            throw new DomainRuleException(CANNOT_RESERVE_OWN_FOOD_TRUCK);
        }
    }

    public void update(
            String reservationAddress,
            String reservationDetailAddress,
            List<String> reservationDateStrings,
            String operationHour,
            String menu,
            Integer reservationDeposit,
            boolean isUseElectricity,
            String etcRequest
    ) {
        this.reservationInfo.updateReservationInfo(
                reservationAddress,
                reservationDetailAddress,
                ReservationDateList.fromJson(reservationDateStrings),
                operationHour,
                menu,
                reservationDeposit,
                isUseElectricity,
                etcRequest
        );
    }

    public void updateStatus(ReservationStatus newStatus) {
        // status 순서가 올바른지 검증 (PENDING -> CONFIRMED_REQUESTED -> CONFIRMED -> CANCELLED_REQUESTED -> CANCELLED)
        if (this.reservationStatus.isInValidStatusTransitionFrom(newStatus)) {
            throw new DomainRuleException(INVALID_RESERVATION_STATUS_TRANSITION);
        }
        this.reservationStatus = newStatus;
    }
}
