package konkuk.chacall.domain.owner.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterBankAccountRequest(
        @NotBlank(message = "은행명을 입력해야 합니다.")
        String bankName,

        @NotBlank(message = "예금주명을 입력해야 합니다.")
        String accountHolderName,

        @NotBlank(message = "계좌번호를 입력해야 합니다.")
        String accountNumber
) {
}
