package konkuk.chacall.domain.owner.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateBankAccountRequest(
        @NotBlank(message = "은행명은 비어있을 수 없습니다.")
        String bankName,

        @NotBlank(message = "예금주명은 비어있을 수 없습니다.")
        String accountHolderName,

        @NotBlank(message = "계좌번호는 비어있을 수 없습니다.")
        String accountNumber
) {
}
