package konkuk.chacall.domain.owner.presentation;

import jakarta.validation.Valid;
import konkuk.chacall.domain.owner.application.OwnerService;
import konkuk.chacall.domain.owner.presentation.dto.request.RegisterBankAccountRequest;
import konkuk.chacall.domain.owner.presentation.dto.response.BankAccountResponse;
import konkuk.chacall.global.common.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owners")
@Slf4j
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping("/me/bank-accounts")
    public BaseResponse<Void> registerBankAccount(
            @Valid RegisterBankAccountRequest registerBankAccountRequest
    ) {
        // todo 추후에 토큰 추가될 시 id 값은 토큰에서 추출하여 전달
        ownerService.registerBankAccount(registerBankAccountRequest, 1L);

        return BaseResponse.ok(null);
    }

    @GetMapping("/me/bank-accounts")
    public BaseResponse<BankAccountResponse> getBankAccount() {
        // todo 추후에 토큰 추가될 시 id 값은 토큰에서 추출하여 전달
        return BaseResponse.ok(ownerService.getBankAccount(1L));
    }
}
