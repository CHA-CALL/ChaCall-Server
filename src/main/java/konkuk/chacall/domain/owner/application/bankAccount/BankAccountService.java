package konkuk.chacall.domain.owner.application.bankAccount;

import konkuk.chacall.domain.owner.domain.model.BankAccount;
import konkuk.chacall.domain.owner.domain.repository.BankAccountRepository;
import konkuk.chacall.domain.owner.presentation.dto.request.RegisterBankAccountRequest;
import konkuk.chacall.domain.owner.presentation.dto.request.UpdateBankAccountRequest;
import konkuk.chacall.domain.owner.presentation.dto.response.BankAccountResponse;
import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.domain.user.domain.repository.UserRepository;
import konkuk.chacall.global.common.domain.BaseStatus;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    @Transactional
    public void registerBankAccount(RegisterBankAccountRequest request, User owner) {

        // 해당 유저의 계좌가 이미 있는지 확인
        if (bankAccountRepository.existsByOwner_UserId(owner.getUserId())) {
            throw new DomainRuleException(ErrorCode.BANK_ACCOUNT_ALREADY_EXISTS_FOR_USER);
        }

        // 중복 계좌 검증
        if (bankAccountRepository.existsByAccountNumber(request.accountNumber())) {
            throw new DomainRuleException(ErrorCode.BANK_ACCOUNT_ALREADY_EXISTS);
        }

        BankAccount bankAccount = BankAccount.builder()
                .bankName(request.bankName())
                .accountHolderName(request.accountHolderName())
                .accountNumber(request.accountNumber())
                .owner(owner)
                .build();

        bankAccountRepository.save(bankAccount);
    }


    public BankAccountResponse getBankAccount(Long ownerId) {
        // 계좌 조회
        BankAccount bankAccount = bankAccountRepository.findByOwner_UserId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.BANK_ACCOUNT_NOT_FOUND));

        return BankAccountResponse.from(bankAccount);
    }


    @Transactional
    public void updateBankAccount(Long ownerId, Long bankAccountId, UpdateBankAccountRequest request) {
        // 계좌 존재 여부 재검증
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.BANK_ACCOUNT_NOT_FOUND));

        // 수정하려는 계좌번호가 현재와 다를 경우에만, 시스템 전체에서 중복되는지 검증
        if (!bankAccount.getAccountNumber().equals(request.accountNumber())) {
            if (bankAccountRepository.existsByAccountNumber(request.accountNumber())) {
                throw new DomainRuleException(ErrorCode.BANK_ACCOUNT_ALREADY_EXISTS);
            }
        }

        // 엔티티의 update 메서드를 호출하여 정보를 수정
        bankAccount.update(
                request.bankName(),
                request.accountHolderName(),
                request.accountNumber()
        );
    }
}
