package konkuk.chacall.domain.owner.application.reservation;

import konkuk.chacall.domain.owner.application.reservation.OwnerReservationService;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckInfo;
import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.repository.ReservationRepository;
import konkuk.chacall.domain.reservation.domain.value.ReservationInfo;
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus;
import konkuk.chacall.domain.reservation.domain.value.ReservationViewType;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.domain.user.domain.repository.UserRepository;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.exception.AuthException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("OwnerReservationService 테스트")
class OwnerReservationServiceTest {

    @InjectMocks
    private OwnerReservationService ownerReservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User owner;

    @Mock
    private User member;

    @Mock
    private Reservation reservation;

    @Nested
    @DisplayName("사장님 예약 내역 목록 조회 시나리오")
    class GetOwnerReservationsScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상 요청 시 해당 상태의 예약 목록을 페이지네이션하여 반환한다")
            void getReservationsSuccess() {
                // given
                FoodTruck foodTruck = mock(FoodTruck.class);
                FoodTruckInfo foodTruckInfo = mock(FoodTruckInfo.class);
                ReservationInfo reservationInfo = mock(ReservationInfo.class);

                Slice<Reservation> reservationSlice = new SliceImpl<>(List.of(reservation));
                given(reservationRepository.findOwnerReservationsByStatusWithCursor(anyLong(), any(Set.class), anyLong(), any(Pageable.class)))
                        .willReturn(reservationSlice);
                given(reservation.getMember()).willReturn(member);
                given(member.getUserId()).willReturn(2L);
                given(userRepository.findAllByUserIdInAndRoleAndStatus(anyList(), any(), any())).willReturn(List.of(member));
                given(reservation.getReservationId()).willReturn(1L);
                given(member.getProfileImageUrl()).willReturn("url");
                given(member.getName()).willReturn("이름");
                given(reservation.getReservationInfo()).willReturn(reservationInfo);
                given(reservationInfo.getFullAddress()).willReturn("주소");
                given(reservationInfo.getFormattedDateTimeInfos()).willReturn(Collections.emptyList());
                given(reservation.getFoodTruck()).willReturn(foodTruck);
                given(foodTruck.getFoodTruckInfo()).willReturn(foodTruckInfo);
                given(foodTruckInfo.getName()).willReturn("푸드트럭");
                given(reservation.getReservationStatus()).willReturn(ReservationStatus.PENDING);

                // when
                CursorPagingResponse<?> response = ownerReservationService.getOwnerReservations(1L, ReservationViewType.CONFIRM_REQUEST, 10L, 10);

                // then
                assertThat(response.content()).hasSize(1);
                verify(reservationRepository).findOwnerReservationsByStatusWithCursor(anyLong(), any(Set.class), anyLong(), any(Pageable.class));
                verify(userRepository).findAllByUserIdInAndRoleAndStatus(anyList(), any(), any());
            }
        }
    }

    @Nested
    @DisplayName("사장님 예약 내역 상세 조회 시나리오")
    class GetReservationDetailScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("푸드트럭 소유주가 자신의 예약 상세 내역을 성공적으로 조회한다")
            void getDetailByOwnerSuccess() {
                // given
                FoodTruck foodTruck = mock(FoodTruck.class);
                FoodTruckInfo foodTruckInfo = mock(FoodTruckInfo.class);
                ReservationInfo reservationInfo = mock(ReservationInfo.class);
                given(reservationRepository.findByIdWithDetails(anyLong())).willReturn(Optional.of(reservation));
                doNothing().when(reservation).validateFoodTruckOwner(anyLong());
                given(reservation.getMember()).willReturn(member);
                given(reservation.getFoodTruck()).willReturn(foodTruck);
                given(foodTruck.getFoodTruckInfo()).willReturn(foodTruckInfo);
                given(foodTruckInfo.getName()).willReturn("푸드트럭");
                given(member.getProfileImageUrl()).willReturn("url");
                given(member.getName()).willReturn("이름");
                given(reservation.getReservationInfo()).willReturn(reservationInfo);
                given(reservationInfo.getFullAddress()).willReturn("주소");
                given(reservationInfo.getFormattedDateTimeInfos()).willReturn(Collections.emptyList());
                given(reservation.getReservationStatus()).willReturn(ReservationStatus.PENDING);
                given(reservation.getPdfUrl()).willReturn("url");
                given(reservationInfo.getMenu()).willReturn("메뉴");
                given(reservationInfo.getDeposit()).willReturn(10000);
                given(reservationInfo.parsingIsUserElectricity()).willReturn("사용");
                given(reservationInfo.getEtcRequest()).willReturn("요청사항");

                // when
                ownerReservationService.getReservationDetail(1L, 1L);

                // then
                verify(reservationRepository).findByIdWithDetails(anyLong());
                verify(reservation).validateFoodTruckOwner(anyLong());
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 예약 ID로 요청 시 RESERVATION_NOT_FOUND 예외를 발생시킨다")
            void getDetailWithInvalidReservationIdFail() {
                // given
                given(reservationRepository.findByIdWithDetails(anyLong())).willReturn(Optional.empty());

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> ownerReservationService.getReservationDetail(1L, 99L));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
            }

            @Test
            @DisplayName("소유주가 아닌 사용자가 요청 시 AUTH_FORBIDDEN 예외를 발생시킨다")
            void getDetailByNotOwnerFail() {
                // given
                given(reservationRepository.findByIdWithDetails(anyLong())).willReturn(Optional.of(reservation));
                doThrow(new AuthException(ErrorCode.USER_FORBIDDEN)).when(reservation).validateFoodTruckOwner(anyLong());

                // when
                AuthException ex = assertThrows(AuthException.class,
                        () -> ownerReservationService.getReservationDetail(2L, 1L));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_FORBIDDEN);
            }
        }
    }
}
