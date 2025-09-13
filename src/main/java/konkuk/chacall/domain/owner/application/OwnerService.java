package konkuk.chacall.domain.owner.application;

import konkuk.chacall.domain.owner.application.bankAccount.BankAccountService;
import konkuk.chacall.domain.owner.presentation.dto.request.RegisterBankAccountRequest;
import konkuk.chacall.domain.owner.presentation.dto.response.BankAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OwnerService {

    private final BankAccountService bankAccountService;

    public void registerBankAccount(RegisterBankAccountRequest request, Long ownerId) {
        bankAccountService.registerBankAccount(request, ownerId);
    }

    public BankAccountResponse getBankAccount(Long ownerId) {
        return bankAccountService.getBankAccount(ownerId);
    }
}
