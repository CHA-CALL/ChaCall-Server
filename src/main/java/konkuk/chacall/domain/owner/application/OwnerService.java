package konkuk.chacall.domain.owner.application;

import konkuk.chacall.domain.owner.application.bankAccount.BankAccountService;
import konkuk.chacall.domain.owner.application.chatTemplate.ChatTemplateService;
import konkuk.chacall.domain.owner.application.validator.OwnerValidator;
import konkuk.chacall.domain.owner.presentation.dto.request.RegisterBankAccountRequest;
import konkuk.chacall.domain.owner.presentation.dto.request.RegisterChatTemplateRequest;
import konkuk.chacall.domain.owner.presentation.dto.request.UpdateBankAccountRequest;
import konkuk.chacall.domain.owner.presentation.dto.response.BankAccountResponse;
import konkuk.chacall.domain.owner.presentation.dto.response.ChatTemplateResponse;
import konkuk.chacall.domain.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OwnerService {

    private final BankAccountService bankAccountService;
    private final ChatTemplateService chatTemplateService;

    // 파사드에서 사장님 검증을 거침으로써 서비스 로직에서는 사장님 검증에 신경쓰지 않도록 책임 분리
    private final OwnerValidator ownerValidator;

    public void registerBankAccount(RegisterBankAccountRequest request, Long ownerId) {
        // 사장님인지 먼저 검증
        User owner = ownerValidator.validateAndGetOwner(ownerId);

        // 검증된 유저 정보를 넘겨 계좌 등록 로직 호출
        bankAccountService.registerBankAccount(request, owner);
    }

    public BankAccountResponse getBankAccount(Long ownerId) {
        // 사장님인지 먼저 검증
        ownerValidator.validateAndGetOwner(ownerId);

        // 계좌 조회 로직 호출
        return bankAccountService.getBankAccount(ownerId);
    }

    public void updateBankAccount(Long ownerId, Long bankAccountId, UpdateBankAccountRequest request) {
        // 사장님인지 먼저 검증
        ownerValidator.validateAndGetOwner(ownerId);

        // 계좌 수정 로직 호출
        bankAccountService.updateBankAccount(ownerId, bankAccountId, request);
    }

    public void deleteBankAccount(Long ownerId, Long bankAccountId) {
        // 사장님인지 먼저 검증
        ownerValidator.validateAndGetOwner(ownerId);

        // 계좌 삭제 로직 호출
        bankAccountService.deleteBankAccount(ownerId, bankAccountId);
    }


    public void registerChatTemplate(RegisterChatTemplateRequest request, Long ownerId) {
        // 사장님인지 먼저 검증
        User owner = ownerValidator.validateAndGetOwner(ownerId);

        // 자주 쓰는 채팅 등록 로직 호출
        chatTemplateService.registerChatTemplate(request, owner);
    }

    public List<ChatTemplateResponse> getChatTemplates(Long ownerId) {
        // 사장님인지 먼저 검증
        ownerValidator.validateAndGetOwner(ownerId);

        // 자주 쓰는 채팅 목록 조회 로직 호출
        return chatTemplateService.getChatTemplates(ownerId);
    }

}
