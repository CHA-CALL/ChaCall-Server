package konkuk.chacall.domain.foodtruck.application.info;

import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.AvailableDateRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckServiceAreaRepository;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.DateRangeRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.request.UpdateFoodTruckInfoRequest;
import konkuk.chacall.domain.foodtruck.presentation.dto.response.FoodTruckDetailResponse;
import konkuk.chacall.domain.foodtruck.domain.value.AvailableQuantity;
import konkuk.chacall.domain.foodtruck.domain.value.NeedElectricity;
import konkuk.chacall.domain.foodtruck.domain.value.PaymentMethod;
import konkuk.chacall.domain.foodtruck.domain.value.AvailableQuantity;
import konkuk.chacall.domain.foodtruck.domain.value.FoodTruckInfo;
import konkuk.chacall.domain.foodtruck.domain.value.MenuCategoryList;
import konkuk.chacall.domain.foodtruck.domain.value.NeedElectricity;
import konkuk.chacall.domain.foodtruck.domain.value.PaymentMethod;
import konkuk.chacall.domain.foodtruck.domain.value.PhotoUrlList;
import konkuk.chacall.domain.foodtruck.domain.value.RatingInfo;
import konkuk.chacall.domain.member.domain.repository.SavedFoodTruckRepository;
import konkuk.chacall.domain.region.domain.model.Region;
import konkuk.chacall.domain.region.domain.repository.RegionRepository;
import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.AuthException;
import konkuk.chacall.global.common.exception.DomainRuleException;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FoodTruckInfoService 테스트")
class FoodTruckInfoServiceTest {

    @InjectMocks
    private FoodTruckInfoService foodTruckInfoService;

    @Mock
    private FoodTruckRepository foodTruckRepository;
    @Mock
    private SavedFoodTruckRepository savedFoodTruckRepository;
    @Mock
    private FoodTruckServiceAreaRepository foodTruckServiceAreaRepository;
    @Mock
    private AvailableDateRepository availableDateRepository;
    @Mock
    private RegionRepository regionRepository;

    @Mock
    private User owner;
    @Mock
    private User member;
    @Mock
    private FoodTruck foodTruck;

    private UpdateFoodTruckInfoRequest request;

    @BeforeEach
    void setUp() {
        request = new UpdateFoodTruckInfoRequest(
                "새로운 푸드트럭",
                "설명",
                "010-1234-5678",
                "10:00-22:00",
                false,
                Set.of(1L),
                Collections.emptyList(),
                AvailableQuantity.LESS_THAN_100,
                NeedElectricity.REQUIRED,
                PaymentMethod.BANK_TRANSFER,
                List.of(new DateRangeRequest(LocalDate.now(), LocalDate.now())),
                Collections.emptyList(),
                "매일 운영",
                "옵션"
        );
    }

    @Nested
    @DisplayName("푸드트럭 이름 중복 확인 시나리오")
    class IsNameDuplicatedScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("이름이 중복될 경우 true를 반환한다")
            void returnsTrueWhenNameIsDuplicated() {
                // given
                given(foodTruckRepository.existsByFoodTruckInfo_Name(anyString())).willReturn(true);
                // when
                boolean result = foodTruckInfoService.isNameDuplicated("중복이름");
                // then
                assertThat(result).isTrue();
            }

            @Test
            @DisplayName("이름이 중복되지 않을 경우 false를 반환한다")
            void returnsFalseWhenNameIsNotDuplicated() {
                // given
                given(foodTruckRepository.existsByFoodTruckInfo_Name(anyString())).willReturn(false);
                // when
                boolean result = foodTruckInfoService.isNameDuplicated("새이름");
                // then
                assertThat(result).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("내 푸드트럭 정보 수정 시나리오")
    class UpdateMyFoodTruckInfoScenario {

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상 요청 시 푸드트럭 정보, 운영 지역, 운영 날짜를 모두 업데이트한다")
            void updateMyFoodTruckInfoSuccess() {
                // given
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                given(owner.getUserId()).willReturn(1L);
                doNothing().when(foodTruck).validateOwner(anyLong());
                doNothing().when(foodTruck).validateApprovedStatus();
                doNothing().when(foodTruck).updateFoodTruckInfo(any(), any(), any(), any(), anyBoolean(), any(), any(), any(), any(NeedElectricity.class), any(), any(), any());
                doNothing().when(foodTruck).permitChangeViewStatus();
                given(foodTruckServiceAreaRepository.findAllByFoodTruckId(anyLong())).willReturn(Collections.emptyList());
                doNothing().when(availableDateRepository).deleteAllByFoodTruckId(anyLong());
                given(regionRepository.findById(anyLong())).willReturn(Optional.of(mock(Region.class)));


                // when
                Long foodTruckId = foodTruckInfoService.updateMyFoodTruckInfo(owner, 1L, request);

                // then
                assertThat(foodTruckId).isNotNull();
                verify(foodTruckRepository).findById(anyLong());
                verify(foodTruck).validateOwner(anyLong());
                verify(foodTruck).validateApprovedStatus();
                verify(foodTruck).updateFoodTruckInfo(any(), any(), any(), any(), anyBoolean(), any(), any(), any(), any(NeedElectricity.class), any(), any(), any());
                verify(availableDateRepository).deleteAllByFoodTruckId(anyLong());
                verify(availableDateRepository).saveAll(any());
                verify(foodTruckServiceAreaRepository).saveAll(any());
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 푸드트럭 ID로 요청 시 FOOD_TRUCK_NOT_FOUND 예외를 발생시킨다")
            void updateWithInvalidFoodTruckIdFail() {
                // given
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.empty());
                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> foodTruckInfoService.updateMyFoodTruckInfo(owner, 1L, request));
                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FOOD_TRUCK_NOT_FOUND);
            }

            @Test
            @DisplayName("소유주가 아닌 사용자가 요청 시 AUTH_FORBIDDEN 예외를 발생시킨다")
            void updateByNotOwnerFail() {
                // given
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                given(owner.getUserId()).willReturn(1L);
                doThrow(new AuthException(ErrorCode.USER_FORBIDDEN)).when(foodTruck).validateOwner(anyLong());
                // when
                AuthException ex = assertThrows(AuthException.class,
                        () -> foodTruckInfoService.updateMyFoodTruckInfo(owner, 1L, request));
                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_FORBIDDEN);
            }

            @Test
            @DisplayName("승인되지 않은 푸드트럭에 요청 시 BUSINESS_LOGIC_EXCEPTION 예외를 발생시킨다")
            void updateOnNotApprovedFoodTruckFail() {
                // given
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                given(owner.getUserId()).willReturn(1L);
                doNothing().when(foodTruck).validateOwner(anyLong());
                doThrow(new DomainRuleException(ErrorCode.FOOD_TRUCK_NOT_APPROVED)).when(foodTruck).validateApprovedStatus();
                // when
                DomainRuleException ex = assertThrows(DomainRuleException.class,
                        () -> foodTruckInfoService.updateMyFoodTruckInfo(owner, 1L, request));
                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FOOD_TRUCK_NOT_APPROVED);
            }
        }
    }

    @Nested
    @DisplayName("푸드트럭 상세 조회 시나리오")
    class GetFoodTruckDetailsScenario {

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("멤버가 조회 시 정상적으로 상세 정보를 반환한다")
            void getDetailsByMemberSuccess() {
                // given
                FoodTruckInfo foodTruckInfo = mock(FoodTruckInfo.class);
                PhotoUrlList photoUrlList = mock(PhotoUrlList.class);
                MenuCategoryList menuCategoryList = mock(MenuCategoryList.class);
                AvailableQuantity availableQuantity = mock(AvailableQuantity.class);
                NeedElectricity needElectricity = mock(NeedElectricity.class);
                PaymentMethod paymentMethod = mock(PaymentMethod.class);
                RatingInfo ratingInfo = mock(RatingInfo.class);

                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                doNothing().when(foodTruck).validateApprovedStatus();
                given(member.getRole()).willReturn(Role.MEMBER);
                doNothing().when(foodTruck).validateViewableStatusForMember();
                given(savedFoodTruckRepository.existsByMemberIdAndFoodTruckId(anyLong(), anyLong())).willReturn(true);
                given(foodTruckServiceAreaRepository.findAllByFoodTruckId(anyLong())).willReturn(Collections.emptyList());
                given(availableDateRepository.findAllByFoodTruckId(anyLong())).willReturn(Collections.emptyList());
                given(foodTruck.getFoodTruckInfo()).willReturn(foodTruckInfo);
                given(foodTruckInfo.getFoodTruckPhotoList()).willReturn(photoUrlList);
                given(photoUrlList.getUrls()).willReturn(Collections.emptyList());
                given(foodTruckInfo.getMenuCategoryList()).willReturn(menuCategoryList);
                given(menuCategoryList.getMenuCategoryLabelList()).willReturn(Collections.emptyList());
                given(foodTruckInfo.getAvailableQuantity()).willReturn(availableQuantity);
                given(availableQuantity.getValue()).willReturn("value");
                given(foodTruckInfo.getNeedElectricity()).willReturn(needElectricity);
                given(needElectricity.getValue()).willReturn("value");
                given(foodTruckInfo.getPaymentMethod()).willReturn(paymentMethod);
                given(paymentMethod.getValue()).willReturn("value");
                given(foodTruck.getRatingInfo()).willReturn(ratingInfo);
                given(ratingInfo.getAverageRating()).willReturn(4.5);
                given(foodTruck.getServiceAreas(any())).willReturn("service areas");
                given(foodTruck.getAvailableDates(any())).willReturn(Collections.emptyList());


                // when
                FoodTruckDetailResponse response = foodTruckInfoService.getFoodTruckDetails(member, 1L);

                // then
                assertThat(response).isNotNull();
                verify(foodTruckRepository).findById(anyLong());
                verify(foodTruck).validateApprovedStatus();
                verify(foodTruck).validateViewableStatusForMember();
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 푸드트럭 ID로 요청 시 FOOD_TRUCK_NOT_FOUND 예외를 발생시킨다")
            void getDetailsWithInvalidIdFail() {
                // given
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.empty());
                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> foodTruckInfoService.getFoodTruckDetails(member, 1L));
                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FOOD_TRUCK_NOT_FOUND);
            }

            @Test
            @DisplayName("비공개 상태의 푸드트럭을 멤버가 조회 시 BUSINESS_LOGIC_EXCEPTION 예외를 발생시킨다")
            void getDetailsOfPrivateFoodTruckByMemberFail() {
                // given
                given(foodTruckRepository.findById(anyLong())).willReturn(Optional.of(foodTruck));
                doNothing().when(foodTruck).validateApprovedStatus();
                given(member.getRole()).willReturn(Role.MEMBER);
                doThrow(new DomainRuleException(ErrorCode.FOOD_TRUCK_NOT_VIEWABLE)).when(foodTruck).validateViewableStatusForMember();

                // when
                DomainRuleException ex = assertThrows(DomainRuleException.class,
                        () -> foodTruckInfoService.getFoodTruckDetails(member, 1L));
                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FOOD_TRUCK_NOT_VIEWABLE);
            }
        }
    }
}
