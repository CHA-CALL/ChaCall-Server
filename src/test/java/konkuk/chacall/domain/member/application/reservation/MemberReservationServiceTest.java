package konkuk.chacall.domain.member.application.reservation;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckInfo;
import konkuk.chacall.domain.foodtruck.domain.value.PhotoUrlList;
import konkuk.chacall.domain.member.domain.repository.RatingRepository;
import konkuk.chacall.domain.member.presentation.dto.response.MemberReservationDetailResponse;
import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.repository.ReservationRepository;
import konkuk.chacall.domain.reservation.domain.value.ReservationViewType;
import konkuk.chacall.domain.user.domain.model.User;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberReservationService 테스트")
class MemberReservationServiceTest {

    @InjectMocks
    private MemberReservationService memberReservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private User member;

    @Mock
    private Reservation reservation;

    @Nested
    @DisplayName("멤버 예약 내역 목록 조회 시나리오")
    class GetMemberReservationsScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상 요청 시 해당 상태의 예약 목록을 페이지네이션하여 반환한다")
            void getReservationsSuccess() {
                // given
                FoodTruck foodTruck = mock(FoodTruck.class);
                FoodTruckInfo foodTruckInfo = mock(FoodTruckInfo.class);
                konkuk.chacall.domain.reservation.domain.value.ReservationInfo reservationInfo = mock(konkuk.chacall.domain.reservation.domain.value.ReservationInfo.class);
                PhotoUrlList photoUrlList = mock(PhotoUrlList.class);

                Slice<Reservation> reservationSlice = new SliceImpl<>(List.of(reservation));
                given(member.getUserId()).willReturn(1L);
                given(reservationRepository.findMemberReservationsByStatusWithCursor(anyLong(), any(Set.class), anyLong(), any(Pageable.class)))
                        .willReturn(reservationSlice);
                given(reservation.getFoodTruck()).willReturn(foodTruck);
                given(foodTruck.getFoodTruckInfo()).willReturn(foodTruckInfo);
                given(foodTruckInfo.getName()).willReturn("푸드트럭");
                given(reservation.getReservationInfo()).willReturn(reservationInfo);
                given(reservationInfo.getFormattedDateTimeInfos()).willReturn(Collections.emptyList());
                given(reservation.getReservationStatus()).willReturn(konkuk.chacall.domain.reservation.domain.value.ReservationStatus.PENDING);
                given(foodTruckInfo.getFoodTruckPhotoList()).willReturn(photoUrlList);
                given(photoUrlList.getMainPhotoUrl()).willReturn("url");
                given(reservationInfo.getFullAddress()).willReturn("주소");
                given(reservation.getReservationId()).willReturn(1L);


                // when
                CursorPagingResponse<?> response = memberReservationService.getMemberReservations(member, ReservationViewType.UPCOMING, 10L, 10);

                // then
                assertThat(response.content()).hasSize(1);
                verify(reservationRepository).findMemberReservationsByStatusWithCursor(anyLong(), any(Set.class), anyLong(), any(Pageable.class));
            }
        }
    }

    @Nested
    @DisplayName("멤버 예약 내역 상세 조회 시나리오")
    class GetMemberReservationDetailScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("예약 멤버가 자신의 예약 상세 내역을 성공적으로 조회한다")
            void getDetailByMemberSuccess() {
                // given
                FoodTruck foodTruck = mock(FoodTruck.class);
                FoodTruckInfo foodTruckInfo = mock(FoodTruckInfo.class);
                PhotoUrlList photoUrlList = mock(PhotoUrlList.class);

                given(reservationRepository.findByIdWithDetails(anyLong())).willReturn(Optional.of(reservation));
                given(member.getUserId()).willReturn(1L);
                doNothing().when(reservation).validateReservedBy(anyLong());
                given(ratingRepository.existsByMemberAndReservation(any(User.class), any(Reservation.class))).willReturn(true);
                given(reservation.getFoodTruck()).willReturn(foodTruck);
                given(foodTruck.getFoodTruckInfo()).willReturn(foodTruckInfo);
                given(foodTruckInfo.getFoodTruckPhotoList()).willReturn(photoUrlList);
                given(photoUrlList.getMainPhotoUrl()).willReturn("url");
                given(reservation.getReservationInfo()).willReturn(mock(konkuk.chacall.domain.reservation.domain.value.ReservationInfo.class));
                given(reservation.getReservationInfo().getFormattedDateTimeInfos()).willReturn(Collections.emptyList());
                given(reservation.getReservationStatus()).willReturn(konkuk.chacall.domain.reservation.domain.value.ReservationStatus.PENDING);


                // when
                MemberReservationDetailResponse response = memberReservationService.getMemberReservationDetail(1L, member);

                // then
                assertThat(response).isNotNull();
                assertThat(response.reviewRequired()).isTrue();
                verify(reservationRepository).findByIdWithDetails(anyLong());
                verify(reservation).validateReservedBy(anyLong());
                verify(ratingRepository).existsByMemberAndReservation(any(User.class), any(Reservation.class));
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
                        () -> memberReservationService.getMemberReservationDetail(99L, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
            }

            @Test
            @DisplayName("예약 멤버가 아닌 사용자가 요청 시 AUTH_FORBIDDEN 예외를 발생시킨다")
            void getDetailByNotMemberFail() {
                // given
                given(reservationRepository.findByIdWithDetails(anyLong())).willReturn(Optional.of(reservation));
                given(member.getUserId()).willReturn(2L);
                doThrow(new AuthException(ErrorCode.USER_FORBIDDEN)).when(reservation).validateReservedBy(anyLong());

                // when
                AuthException ex = assertThrows(AuthException.class,
                        () -> memberReservationService.getMemberReservationDetail(1L, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_FORBIDDEN);
            }
        }
    }
}
