package konkuk.chacall.domain.reservation.domain.model;

import jakarta.persistence.*;
import konkuk.chacall.domain.foodtruck.domain.FoodTruck;
import konkuk.chacall.domain.reservation.domain.value.ReservationInfo;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.domain.BaseEntity;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.*;

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

    public boolean isOwnedBy(Long ownerId) {
        return foodTruck.getOwner().getUserId().equals(ownerId);
    }

    public void validateCanBeRatedBy(User member) {
        if (!this.member.equals(member)) {
            throw new DomainRuleException(CANNOT_RATE_RESERVATION_NOT_OWNED);
        }
        if (this.reservationStatus != ReservationStatus.CONFIRMED) {
            throw new DomainRuleException(CANNOT_RATE_UNCONFIRMED_RESERVATION);
        }
    }
}
