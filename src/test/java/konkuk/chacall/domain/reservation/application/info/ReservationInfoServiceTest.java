package konkuk.chacall.domain.reservation.application.info;

import konkuk.chacall.domain.chat.domain.ChatRoom;
import konkuk.chacall.domain.chat.domain.repository.ChatRoomRepository;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.repository.ReservationRepository;
import konkuk.chacall.domain.reservation.presentation.dto.request.CreateReservationRequest;
import konkuk.chacall.domain.reservation.presentation.dto.request.UpdateReservationRequest;
import konkuk.chacall.domain.reservation.presentation.dto.response.ReservationResponse;
import konkuk.chacall.domain.reservation.domain.value.ReservationInfo; // Added this line
import konkuk.chacall.domain.reservation.domain.value.ReservationDateList; // Added this line
import konkuk.chacall.domain.reservation.domain.value.ReservationStatus; // Added this line
import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.AuthException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
@DisplayName("ReservationInfoService 테스트")
class ReservationInfoServiceTest {

    @InjectMocks
    private ReservationInfoService reservationInfoService;

    @Mock
    private FoodTruckRepository foodTruckRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private User owner;

    @Mock
    private User member;

    @Mock
    private User otherUser;

    @Mock
    private FoodTruck foodTruck;

    @Mock
    private ChatRoom chatRoom;

    @Mock
    private Reservation reservation;

    @Mock
    private ReservationInfo reservationInfo; // New mock for ReservationInfo

    private CreateReservationRequest createRequest;
    private UpdateReservationRequest updateRequest;

    @BeforeEach
    void setUp() {
        given(owner.getUserId()).willReturn(1L);
        given(member.getUserId()).willReturn(2L);
        given(owner.getRole()).willReturn(Role.OWNER);
        given(member.getRole()).willReturn(Role.MEMBER);

        createRequest = new CreateReservationRequest(
                1L,
                1L,
                1L,
                "서울시 광진구",
                "아크ro텔",
                List.of(LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd")) + " ~ " + LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd"))),
                "10:00-18:00",
                "떡볶이, 튀김",
                10000,
                true,
                ""
        );
        updateRequest = new UpdateReservationRequest(
                "서울시 성동구",
                "왕십리역",
                List.of(LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd")) + " ~ " + LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd"))),
                "12:00-20:00",
                "순대, 어묵",
                15000,
                false,
                "조용히"
        );
        mockReservation();
    }

    private void mockReservation() {
        given(reservation.getReservationId()).willReturn(1L);
        given(reservation.getReservationStatus()).willReturn(ReservationStatus.PENDING); // Added this line
        given(reservation.getReservationInfo()).willReturn(reservationInfo); // Stub reservation.getReservationInfo()
        given(reservationInfo.getAddress()).willReturn("서울시 광진구");
        given(reservationInfo.getDetailAddress()).willReturn("아크로텔");
        given(reservationInfo.getReservationDates()).willReturn(ReservationDateList.fromJson(List.of(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + " ~ " + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))));
        given(reservationInfo.getOperationHour()).willReturn("10:00-18:00");
        given(reservationInfo.getMenu()).willReturn("떡볶이, 튀김");
        given(reservationInfo.getDeposit()).willReturn(10000);
        given(reservationInfo.isUseElectricity()).willReturn(true);
        given(reservationInfo.getEtcRequest()).willReturn("");
        given(reservation.getMember()).willReturn(member);
        given(reservation.getFoodTruck()).willReturn(foodTruck);
        given(foodTruck.getOwner()).willReturn(owner); // Stub foodTruck.getOwner()
        given(reservation.getChatRoom()).willReturn(chatRoom);
    }

    @Nested
    @DisplayName("예약 생성 시나리오")
    class CreateReservationScenario {

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상적인 요청 시 예약을 생성하고 생성된 예약 ID를 반환한다")
            void createReservationSuccess() {
                // given
                given(foodTruckRepository.findById(any(Long.class))).willReturn(Optional.of(foodTruck));
                given(chatRoomRepository.findById(any(Long.class))).willReturn(Optional.of(chatRoom));
                given(reservationRepository.save(any(Reservation.class))).willReturn(reservation);


                // when
                Long reservationId = reservationInfoService.createReservation(createRequest, owner, member);

                // then
                assertThat(reservationId).isEqualTo(1L);
                verify(foodTruckRepository).findById(any(Long.class));
                verify(chatRoomRepository).findById(any(Long.class));
                verify(reservationRepository).save(any(Reservation.class));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 푸드트럭 ID로 요청 시 FOOD_TRUCK_NOT_FOUND 예외를 발생시킨다")
            void createReservationWithInvalidFoodTruckFail() {
                // given
                given(foodTruckRepository.findById(any(Long.class))).willReturn(Optional.empty());

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> reservationInfoService.createReservation(createRequest, owner, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FOOD_TRUCK_NOT_FOUND);
            }

            @Test
            @DisplayName("존재하지 않는 채팅방 ID로 요청 시 CHAT_ROOM_NOT_FOUND 예외를 발생시킨다")
            void createReservationWithInvalidChatRoomFail() {
                // given
                given(foodTruckRepository.findById(any(Long.class))).willReturn(Optional.of(foodTruck));
                given(chatRoomRepository.findById(any(Long.class))).willReturn(Optional.empty());

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> reservationInfoService.createReservation(createRequest, owner, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.CHAT_ROOM_NOT_FOUND);
            }
        }
    }

    @Nested
    @DisplayName("예약 단건 조회 시나리오")
    class GetReservationScenario {

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("예약한 멤버가 조회를 요청하면 예약 정보를 반환한다")
            void getReservationByMemberSuccess() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.of(reservation));
                given(member.getRole()).willReturn(Role.MEMBER);
                given(member.getUserId()).willReturn(2L);
                doNothing().when(reservation).validateReservedBy(any(Long.class));

                // when
                ReservationResponse response = reservationInfoService.getReservation(1L, member);

                // then
                assertThat(response).isNotNull();
                verify(reservationRepository).findById(any(Long.class));
            }

            @Test
            @DisplayName("푸드트럭 주인이 조회를 요청하면 예약 정보를 반환한다")
            void getReservationByOwnerSuccess() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.of(reservation));
                given(owner.getRole()).willReturn(Role.OWNER);
                given(owner.getUserId()).willReturn(1L);
                doNothing().when(reservation).validateFoodTruckOwner(any(Long.class));

                // when
                ReservationResponse response = reservationInfoService.getReservation(1L, owner);

                // then
                assertThat(response).isNotNull();
                verify(reservationRepository).findById(any(Long.class));
                verify(reservation).validateFoodTruckOwner(any(Long.class));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 예약 ID로 요청 시 RESERVATION_NOT_FOUND 예외를 발생시킨다")
            void getReservationWithInvalidIdFail() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.empty());

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> reservationInfoService.getReservation(1L, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
            }

            @Test
            @DisplayName("관련 없는 멤버가 조회를 요청하면 AUTH_FORBIDDEN 예외를 발생시킨다")
            void getReservationByUnrelatedUserFail() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.of(reservation));
                given(otherUser.getRole()).willReturn(Role.MEMBER);
                given(otherUser.getUserId()).willReturn(99L);
                doThrow(new AuthException(ErrorCode.USER_FORBIDDEN)).when(reservation).validateReservedBy(99L);

                // when
                AuthException ex = assertThrows(AuthException.class,
                        () -> reservationInfoService.getReservation(1L, otherUser));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_FORBIDDEN);
            }
        }
    }

    @Nested
    @DisplayName("예약 수정 시나리오")
    class UpdateReservationScenario {

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("예약한 멤버가 수정을 요청하면 예약 정보가 수정된다")
            void updateReservationByMemberSuccess() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.of(reservation));
                given(member.getRole()).willReturn(Role.MEMBER);
                given(member.getUserId()).willReturn(2L);
                doNothing().when(reservation).validateReservedBy(any(Long.class));

                // when
                reservationInfoService.updateReservation(1L, updateRequest, member);

                // then
                verify(reservationRepository).findById(any(Long.class));
                verify(reservation).validateReservedBy(any(Long.class));
            }

            @Test
            @DisplayName("푸드트럭 주인이 수정을 요청하면 예약 정보가 수정된다")
            void updateReservationByOwnerSuccess() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.of(reservation));
                given(owner.getRole()).willReturn(Role.OWNER);
                given(owner.getUserId()).willReturn(1L);
                doNothing().when(reservation).validateFoodTruckOwner(any(Long.class));

                // when
                reservationInfoService.updateReservation(1L, updateRequest, owner);

                // then
                verify(reservationRepository).findById(any(Long.class));
                verify(reservation).validateFoodTruckOwner(any(Long.class));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 예약 ID로 수정 요청 시 RESERVATION_NOT_FOUND 예외를 발생시킨다")
            void updateReservationWithInvalidIdFail() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.empty());

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> reservationInfoService.updateReservation(1L, updateRequest, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
            }

            @Test
            @DisplayName("관련 없는 사용자가 수정을 요청하면 AUTH_FORBIDDEN 예외를 발생시킨다")
            void updateReservationByUnrelatedUserFail() {
                // given
                given(reservationRepository.findById(any(Long.class))).willReturn(Optional.of(reservation));
                given(otherUser.getRole()).willReturn(Role.MEMBER);
                given(otherUser.getUserId()).willReturn(99L);
                doThrow(new AuthException(ErrorCode.USER_FORBIDDEN)).when(reservation).validateReservedBy(99L);

                // when
                AuthException ex = assertThrows(AuthException.class,
                        () -> reservationInfoService.updateReservation(1L, updateRequest, otherUser));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_FORBIDDEN);
            }
        }
    }
}
