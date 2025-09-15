package konkuk.chacall.domain.owner.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "은행 계좌 수정 요청 DTO")
public record UpdateBankAccountRequest(
        @Schema(description = "은행명", example = "국민은행")
        @NotBlank(message = "은행명은 비어있을 수 없습니다.")
        String bankName,

        @Schema(description = "예금주명", example = "홍길동")
        @NotBlank(message = "예금주명은 비어있을 수 없습니다.")
        String accountHolderName,

        @Schema(description = "계좌번호", example = "123-456-78901234")
        @NotBlank(message = "계좌번호는 비어있을 수 없습니다.")
        String accountNumber
) {
}
