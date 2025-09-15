package konkuk.chacall.domain.reservation.domain.repository;

import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.member m " +                  // 예약자 정보 함께 조회
            "JOIN FETCH r.foodTruck ft " +              // 푸드트럭 정보 함께 조회
            "JOIN FETCH ft.owner o " +                  // 푸드트럭의 사장님 정보 함께 조회
            "WHERE o.userId = :ownerId AND r.reservationStatus = :status ")
    List<Reservation> findOwnerReservationsByStatus(@Param("ownerId") Long ownerId, @Param("status") ReservationStatus status);
}
