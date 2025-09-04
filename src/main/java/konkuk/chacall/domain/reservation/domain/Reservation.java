package konkuk.chacall.domain.reservation.domain;

import jakarta.persistence.*;
import konkuk.chacall.domain.reservation.domain.value.ReservationInfo;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;
import konkuk.chacall.global.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
