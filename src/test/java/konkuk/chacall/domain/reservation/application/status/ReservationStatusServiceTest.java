package konkuk.chacall.domain.reservation.application.status;

import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.repository.ReservationRepository;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;
import konkuk.chacall.domain.reservation.presentation.dto.request.UpdateReservationStatusRequest;
import konkuk.chacall.domain.reservation.presentation.dto.response.ReservationStatusResponse;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.AuthException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import konkuk.chacall.global.common.storage.pdf.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReservationStatusService 테스트")
class ReservationStatusServiceTest {

    @InjectMocks
    private ReservationStatusService reservationStatusService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PdfService pdfService;

    @Mock
    private User owner;

    @Mock
    private User member;

    @Mock
    private User otherUser;

    @Mock
    private Reservation reservation;

    private UpdateReservationStatusRequest request;

    @Nested
    @DisplayName("예약 상태 '확정 요청'으로 변경 시나리오")
    class UpdateToConfirmedRequestedScenario {

        @BeforeEach
        void setUp() {
            request = new UpdateReservationStatusRequest(ReservationStatus.CONFIRMED_REQUESTED);
        }

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("예약 멤버가 요청 시 상태를 '확정 요청'으로 변경한다")
            void updateToConfirmedRequestedByMemberSuccess() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.of(reservation));
                given(member.getUserId()).willReturn(1L);
                doNothing().when(reservation).validateReservedBy(any(Long.class));
                doNothing().when(reservation).updateStatus(any(ReservationStatus.class));
                given(reservation.getReservationStatus()).willReturn(ReservationStatus.CONFIRMED_REQUESTED);

                // when
                ReservationStatusResponse response = reservationStatusService.updateReservationStatusToConfirmedRequested(1L, request, member);

                // then
                assertThat(response.reservationStatus()).isEqualTo(ReservationStatus.CONFIRMED_REQUESTED.getValue());
                verify(reservationRepository).findById(any(Long.class));
                verify(reservation).validateReservedBy(any(Long.class));
                verify(reservation).updateStatus(any(ReservationStatus.class));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 예약 ID로 요청 시 RESERVATION_NOT_FOUND 예외를 발생시킨다")
            void updateToConfirmedRequestedWithInvalidIdFail() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.empty());

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> reservationStatusService.updateReservationStatusToConfirmedRequested(1L, request, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
            }

            @Test
            @DisplayName("푸드트럭 주인이 요청 시 AUTH_FORBIDDEN 예외를 발생시킨다")
            void updateToConfirmedRequestedByOwnerFail() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.of(reservation));
                given(owner.getUserId()).willReturn(2L);
                doThrow(new AuthException(ErrorCode.USER_FORBIDDEN)).when(reservation).validateReservedBy(any(Long.class));

                // when
                AuthException ex = assertThrows(AuthException.class,
                        () -> reservationStatusService.updateReservationStatusToConfirmedRequested(1L, request, owner));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_FORBIDDEN);
            }
        }
    }

    @Nested
    @DisplayName("예약 상태 '확정'으로 변경 시나리오")
    class UpdateToConfirmedScenario {

        @BeforeEach
        void setUp() {
            request = new UpdateReservationStatusRequest(ReservationStatus.CONFIRMED);
        }

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("푸드트럭 주인이 요청 시 상태를 '확정'으로 변경하고 PDF를 생성한다")
            void updateToConfirmedByOwnerSuccess() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.of(reservation));
                given(owner.getUserId()).willReturn(2L);
                doNothing().when(reservation).validateFoodTruckOwner(any(Long.class));
                doNothing().when(reservation).updateStatus(any(ReservationStatus.class));
                given(pdfService.renderAndUpload(any(Reservation.class))).willReturn("pdf_url");
                doNothing().when(reservation).setPdfUrl(any(String.class));
                given(reservation.getReservationStatus()).willReturn(ReservationStatus.CONFIRMED);

                // when
                ReservationStatusResponse response = reservationStatusService.updateReservationStatusToConfirmed(1L, request, owner);

                // then
                assertThat(response.reservationStatus()).isEqualTo(ReservationStatus.CONFIRMED.getValue());
                verify(reservationRepository).findById(any(Long.class));
                verify(reservation).validateFoodTruckOwner(any(Long.class));
                verify(reservation).updateStatus(any(ReservationStatus.class));
                verify(pdfService).renderAndUpload(any(Reservation.class));
                verify(reservation).setPdfUrl(any(String.class));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {

            @Test
            @DisplayName("예약 멤버가 요청 시 AUTH_FORBIDDEN 예외를 발생시킨다")
            void updateToConfirmedByMemberFail() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.of(reservation));
                given(member.getUserId()).willReturn(1L);
                doThrow(new AuthException(ErrorCode.USER_FORBIDDEN)).when(reservation).validateFoodTruckOwner(any(Long.class));

                AuthException ex = assertThrows(AuthException.class,
                        () -> reservationStatusService.updateReservationStatusToConfirmed(1L, request, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_FORBIDDEN);
            }
        }
    }

    @Nested
    @DisplayName("예약 상태 '취소'로 변경 시나리오")
    class UpdateToCancelledScenario {

        @BeforeEach
        void setUp() {
            request = new UpdateReservationStatusRequest(ReservationStatus.CANCELLED);
        }

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("예약 멤버가 요청 시 상태를 '취소'로 변경한다")
            void updateToCancelledByMemberSuccess() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.of(reservation));
                given(member.getUserId()).willReturn(1L);
                doNothing().when(reservation).validateAccessibleBy(any(Long.class));
                doNothing().when(reservation).updateStatus(any(ReservationStatus.class));
                given(reservation.getReservationStatus()).willReturn(ReservationStatus.CANCELLED);

                // when
                ReservationStatusResponse response = reservationStatusService.updateReservationStatusToCancelled(1L, request, member);

                // then
                assertThat(response.reservationStatus()).isEqualTo(ReservationStatus.CANCELLED.getValue());
                verify(reservationRepository).findById(any(Long.class));
                verify(reservation).validateAccessibleBy(any(Long.class));
                verify(reservation).updateStatus(any(ReservationStatus.class));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("관련 없는 사용자가 요청 시 AUTH_FORBIDDEN 예외를 발생시킨다")
            void updateToCancelledByOtherUserFail() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.of(reservation));
                given(otherUser.getUserId()).willReturn(99L);
                doThrow(new AuthException(ErrorCode.USER_FORBIDDEN)).when(reservation).validateAccessibleBy(any(Long.class));

                // when
                AuthException ex = assertThrows(AuthException.class,
                        () -> reservationStatusService.updateReservationStatusToCancelled(1L, request, otherUser));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_FORBIDDEN);
            }
        }
    }

    @Nested
    @DisplayName("예약 상태 조회 시나리오")
    class GetStatusScenario {

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("관련된 사용자가 요청 시 예약 상태를 반환한다")
            void getStatusByAllowedUserSuccess() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.of(reservation));
                given(member.getUserId()).willReturn(1L);
                doNothing().when(reservation).validateAccessibleBy(any(Long.class));
                given(reservation.getReservationStatus()).willReturn(ReservationStatus.PENDING);

                // when
                ReservationStatusResponse response = reservationStatusService.getReservationStatus(1L, member);

                // then
                assertThat(response.reservationStatus()).isEqualTo(ReservationStatus.PENDING.getValue());
                verify(reservationRepository).findById(any(Long.class));
                verify(reservation).validateAccessibleBy(any(Long.class));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 예약 ID로 요청 시 RESERVATION_NOT_FOUND 예외를 발생시킨다")
            void getStatusWithInvalidIdFail() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.empty());

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> reservationStatusService.getReservationStatus(1L, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
            }
        }
    }
}
