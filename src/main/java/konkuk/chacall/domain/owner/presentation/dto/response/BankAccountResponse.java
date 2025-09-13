package konkuk.chacall.domain.owner.presentation.dto.response;

import konkuk.chacall.domain.owner.domain.model.BankAccount;

public record BankAccountResponse(
        Long bankAccountId,
        String bankName,
        String accountHolderName,
        String accountNumber
) {
    public static BankAccountResponse from(BankAccount bankAccount) {
        return new BankAccountResponse(
                bankAccount.getBankAccountId(),
                bankAccount.getBankName(),
                bankAccount.getAccountHolderName(),
                bankAccount.getAccountNumber());
    }
}
