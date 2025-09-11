package konkuk.chacall.domain.owner.presentation.dto;

public record RegisterBankAccountRequest(
        String bankName,
        String accountHolderName,
        String accountNumber
) {
    public static RegisterBankAccountRequest of(
            String bankName,
            String accountHolderName,
            String accountNumber
    ) {
        return new RegisterBankAccountRequest(bankName, accountHolderName, accountNumber);
    }
}
