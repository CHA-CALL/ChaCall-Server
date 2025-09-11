package konkuk.chacall.domain.owner.application.bankAccount;

import konkuk.chacall.domain.owner.domain.model.BankAccount;
import konkuk.chacall.domain.owner.domain.repository.BankAccountRepository;
import konkuk.chacall.domain.owner.presentation.dto.RegisterBankAccountRequest;
import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.domain.user.domain.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    @Transactional
    public void registerBankAccount(RegisterBankAccountRequest request, Long ownerId) {
        // 존재 여부 및 역할 검증 동시에
        User owner = userRepository.findByUserIdAndRole(ownerId, Role.OWNER).orElseThrow(
                () -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        // 중복 계좌 검증
        if (bankAccountRepository.existsByOwner_UserIdAndAccountNumber(ownerId, request.accountNumber())) {
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
}
