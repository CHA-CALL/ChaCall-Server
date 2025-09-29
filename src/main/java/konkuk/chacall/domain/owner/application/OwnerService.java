package konkuk.chacall.domain.owner.application;

import konkuk.chacall.domain.owner.application.bankaccount.BankAccountService;
import konkuk.chacall.domain.owner.application.chattemplate.ChatTemplateService;
import konkuk.chacall.domain.owner.application.myfoodtruck.MyFoodTruckService;
import konkuk.chacall.domain.owner.application.reservation.OwnerReservationService;
import konkuk.chacall.domain.owner.application.validator.OwnerValidator;
import konkuk.chacall.domain.owner.presentation.dto.request.*;
import konkuk.chacall.domain.owner.presentation.dto.response.*;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.dto.CursorPagingRequest;
import konkuk.chacall.global.common.dto.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OwnerService {

    private final BankAccountService bankAccountService;
    private final ChatTemplateService chatTemplateService;
    private final OwnerReservationService ownerReservationService;
    private final MyFoodTruckService myFoodTruckService;

    // 파사드에서 사장님 검증을 거침으로써 서비스 로직에서는 사장님 검증에 신경쓰지 않도록 책임 분리
    private final OwnerValidator ownerValidator;

    @Transactional
    public void registerBankAccount(RegisterBankAccountRequest request, Long ownerId) {
        // 사장님인지 먼저 검증
        User owner = ownerValidator.validateAndGetOwner(ownerId);

        // 검증된 유저 정보를 넘겨 계좌 등록 로직 호출
        bankAccountService.registerBankAccount(request, owner);
    }

    @Transactional(readOnly = true)
    public BankAccountResponse getBankAccount(Long ownerId) {
        // 사장님인지 먼저 검증
        ownerValidator.validateAndGetOwner(ownerId);

        // 계좌 조회 로직 호출
        return bankAccountService.getBankAccount(ownerId);
    }

    @Transactional
    public void updateBankAccount(Long ownerId, Long bankAccountId, UpdateBankAccountRequest request) {
        // 사장님인지 먼저 검증
        ownerValidator.validateAndGetOwner(ownerId);

        // 계좌 수정 로직 호출
        bankAccountService.updateBankAccount(ownerId, bankAccountId, request);
    }

    @Transactional
    public void deleteBankAccount(Long ownerId, Long bankAccountId) {
        // 사장님인지 먼저 검증
        ownerValidator.validateAndGetOwner(ownerId);

        // 계좌 삭제 로직 호출
        bankAccountService.deleteBankAccount(ownerId, bankAccountId);
    }

    @Transactional
    public void registerChatTemplate(RegisterChatTemplateRequest request, Long ownerId) {
        // 사장님인지 먼저 검증
        User owner = ownerValidator.validateAndGetOwner(ownerId);

        // 자주 쓰는 채팅 등록 로직 호출
        chatTemplateService.registerChatTemplate(request, owner);
    }

    @Transactional(readOnly = true)
    public List<ChatTemplateResponse> getChatTemplates(Long ownerId) {
        // 사장님인지 먼저 검증
        ownerValidator.validateAndGetOwner(ownerId);

        // 자주 쓰는 채팅 목록 조회 로직 호출
        return chatTemplateService.getChatTemplates(ownerId);
    }

    @Transactional
    public void updateChatTemplate(UpdateChatTemplateRequest request, Long ownerId, Long chatTemplateId) {
        // 사장님인지 먼저 검증
        ownerValidator.validateAndGetOwner(ownerId);

        // 자주 쓰는 채팅 수정 로직 호출
        chatTemplateService.updateChatTemplate(request, chatTemplateId);
    }

    @Transactional
    public void deleteChatTemplate(Long ownerId, Long chatTemplateId) {
        // 사장님인지 먼저 검증
        ownerValidator.validateAndGetOwner(ownerId);

        // 자주 쓰는 채팅 삭제 로직 호출
        chatTemplateService.deleteChatTemplate(chatTemplateId);
    }

    @Transactional(readOnly = true)
    public CursorPagingResponse<OwnerReservationHistoryResponse> getOwnerReservations(GetReservationHistoryRequest request, Long ownerId) {
        // 사장님인지 먼저 검증
        ownerValidator.validateAndGetOwner(ownerId);

        // 사장님 예약 내역 조회 로직 호출
        CursorPagingRequest cursorPagingRequest = request.pagingOrDefault(SortType.NEWEST);
        return ownerReservationService.getOwnerReservations(ownerId, request.viewType(), cursorPagingRequest.cursor(), cursorPagingRequest.size());
    }

    @Transactional(readOnly = true)
    public OwnerReservationDetailResponse getReservationDetail(Long ownerId, Long reservationId) {
        // 사장님인지 먼저 검증
        ownerValidator.validateAndGetOwner(ownerId);

        // 사장님 예약 내역 상세 조회 로직 호출
        return ownerReservationService.getReservationDetail(ownerId, reservationId);
    }

    @Transactional(readOnly = true)
    public CursorPagingResponse<MyFoodTruckResponse> getMyFoodTrucks(CursorPagingRequest request, Long ownerId) {
        // 사장님인지 먼저 검증
        ownerValidator.validateAndGetOwner(ownerId);

        // 사장님 - 나의 푸드트럭 목록 조회 로직 호출
        return myFoodTruckService.getMyFoodTrucks(request, ownerId);
    }

    @Transactional
    public void deleteMyFoodTruck(Long ownerId, Long foodTruckId) {
        // 사장님인지 먼저 검증
        ownerValidator.validateAndGetOwner(ownerId);

        // 사장님 - 나의 푸드트럭 삭제 로직 호출
        myFoodTruckService.deleteMyFoodTruck(ownerId, foodTruckId);
    }
}
