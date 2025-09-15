package konkuk.chacall.domain.owner.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.chacall.domain.owner.domain.model.BankAccount;

public record BankAccountResponse(
        @Schema(description = "계좌 식별자", example = "1")
        Long bankAccountId,
        @Schema(description = "은행명", example = "기업은행")
        String bankName,
        @Schema(description = "예금주명", example = "홍길동")
        String accountHolderName,
        @Schema(description = "계좌번호", example = "110-1123-123124")
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
