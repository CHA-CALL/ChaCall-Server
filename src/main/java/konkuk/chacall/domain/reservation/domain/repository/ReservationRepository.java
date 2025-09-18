package konkuk.chacall.domain.reservation.domain.repository;

import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

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


    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.member m " +
            "JOIN FETCH r.foodTruck ft " +
            "JOIN FETCH ft.owner o " +
            "WHERE r.reservationId = :reservationId")
    Optional<Reservation> findByIdWithDetails(@Param("reservationId") Long reservationId);


    @EntityGraph(attributePaths = {"foodTruck"})
    @Query("SELECT r FROM Reservation r " +
            "WHERE r.member.userId = :memberId " +
            "AND r.reservationStatus = :status " +
            "AND r.reservationId < :lastCursor " +
            "ORDER BY r.reservationId DESC")
    Slice<Reservation> findMemberReservationsByStatusWithCursor(
            @Param("memberId") Long memberId,
            @Param("status") ReservationStatus status,
            @Param("lastCursor") Long lastCursor,
            Pageable pageable);
}
