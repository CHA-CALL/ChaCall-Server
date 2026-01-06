package konkuk.chacall.domain.member.application.rating;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.member.domain.Rating;
import konkuk.chacall.domain.member.domain.repository.RatingRepository;
import konkuk.chacall.domain.member.presentation.dto.request.RegisterRatingRequest;
import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.repository.ReservationRepository;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RatingService 테스트")
class RatingServiceTest {

    @InjectMocks
    private RatingService ratingService;

    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private FoodTruckRepository foodTruckRepository;
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private User member;
    @Mock
    private FoodTruck foodTruck;
    @Mock
    private Reservation reservation;
    @Mock
    private Rating rating;

    @Nested
    @DisplayName("평점 등록 시나리오")
    class RegisterRatingsScenario {

        private final RegisterRatingRequest request = new RegisterRatingRequest(1L, 1L, "4.5");

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("평가 가능한 예약에 대해 평점을 성공적으로 등록한다")
            void registerRatingSuccess() {
                // given
                given(reservationRepository.findById(anyLong())).willReturn(Optional.of(reservation));
                doNothing().when(reservation).validateCanBeRatedBy(any(User.class));
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                given(ratingRepository.findByMemberAndFoodTruckAndReservationAndIsRatedFalse(any(), any(), any()))
                        .willReturn(Optional.of(rating));
                doNothing().when(rating).registerRating(anyDouble());
                doNothing().when(foodTruck).updateAverageRating(anyDouble());

                // when
                ratingService.registerRatings(request, member);

                // then
                verify(reservation).validateCanBeRatedBy(member);
                verify(rating).registerRating(anyDouble());
                verify(foodTruck).updateAverageRating(anyDouble());
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 예약 ID로 요청 시 RESERVATION_NOT_FOUND 예외를 발생시킨다")
            void registerWithInvalidReservationIdFail() {
                // given
                given(reservationRepository.findById(anyLong())).willReturn(Optional.empty());

                // when
                BusinessException ex = assertThrows(BusinessException.class,
                        () -> ratingService.registerRatings(request, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
            }

            @Test
            @DisplayName("평가할 수 없는 상태의 예약이면 CANNOT_RATE_RESERVATION 예외를 발생시킨다")
            void registerForUnratableReservationFail() {
                // given
                given(reservationRepository.findById(anyLong())).willReturn(Optional.of(reservation));
                doThrow(new DomainRuleException(ErrorCode.CANNOT_RATE_UNCONFIRMED_RESERVATION))
                        .when(reservation).validateCanBeRatedBy(any(User.class));

                // when
                DomainRuleException ex = assertThrows(DomainRuleException.class,
                        () -> ratingService.registerRatings(request, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.CANNOT_RATE_UNCONFIRMED_RESERVATION);
            }

            @Test
            @DisplayName("평가할 대상(isRated=false)이 없으면 RATING_NOT_FOUND 예외를 발생시킨다")
            void registerWhenNoUnratedRatingExistsFail() {
                // given
                given(reservationRepository.findById(anyLong())).willReturn(Optional.of(reservation));
                doNothing().when(reservation).validateCanBeRatedBy(any(User.class));
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                given(ratingRepository.findByMemberAndFoodTruckAndReservationAndIsRatedFalse(any(), any(), any()))
                        .willReturn(Optional.empty());

                // when
                BusinessException ex = assertThrows(BusinessException.class,
                        () -> ratingService.registerRatings(request, member));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.RATING_NOT_FOUND);
            }
        }
    }
}
