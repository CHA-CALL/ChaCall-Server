package konkuk.chacall.domain.reservation.domain.repository;

import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.foodTruck.owner.userId = :ownerId " +
            "AND r.reservationStatus = :status " +
            "AND r.reservationId < :lastCursor " +
            "ORDER BY r.reservationId DESC")
    Slice<Reservation> findOwnerReservationsByStatusWithCursor(
            @Param("ownerId") Long ownerId,
            @Param("status") ReservationStatus status,
            @Param("lastCursor") Long lastCursor,
            Pageable pageable);
}
