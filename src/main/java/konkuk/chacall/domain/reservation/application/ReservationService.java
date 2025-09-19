package konkuk.chacall.domain.reservation.application;

import konkuk.chacall.domain.member.application.validator.MemberValidator;
import konkuk.chacall.domain.owner.application.validator.OwnerValidator;
import konkuk.chacall.domain.reservation.application.reservationinfo.ReservationInfoService;
import konkuk.chacall.domain.reservation.presentation.dto.request.CreateReservationRequest;
import konkuk.chacall.domain.reservation.presentation.dto.response.ReservationResponse;
import konkuk.chacall.domain.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationInfoService reservationInfoService;

    private final OwnerValidator ownerValidator;
    private final MemberValidator memberValidator;

    @Transactional
    public Long createReservation(CreateReservationRequest request, Long ownerId) {
        User owner = ownerValidator.validateAndGetOwner(ownerId);

        return reservationInfoService.createReservation(request, owner);
    }

    public ReservationResponse getReservation(Long reservationId, Long userId) {
        User user = memberValidator.validateAndGetMember(userId);

        return reservationInfoService.getReservation(reservationId, user);
    }
}
