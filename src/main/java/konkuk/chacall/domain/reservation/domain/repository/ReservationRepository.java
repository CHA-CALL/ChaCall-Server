package konkuk.chacall.domain.reservation.domain.repository;

import konkuk.chacall.domain.reservation.domain.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
