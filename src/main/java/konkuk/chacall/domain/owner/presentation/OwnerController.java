package konkuk.chacall.domain.owner.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.chacall.domain.owner.application.OwnerService;
import konkuk.chacall.domain.owner.presentation.dto.request.RegisterBankAccountRequest;
import konkuk.chacall.domain.owner.presentation.dto.request.RegisterChatTemplateRequest;
import konkuk.chacall.domain.owner.presentation.dto.request.UpdateBankAccountRequest;
import konkuk.chacall.domain.owner.presentation.dto.request.UpdateChatTemplateRequest;
import konkuk.chacall.domain.owner.presentation.dto.response.BankAccountResponse;
import konkuk.chacall.global.common.annotation.ExceptionDescription;
import konkuk.chacall.global.common.annotation.UserId;
import konkuk.chacall.domain.owner.presentation.dto.response.ChatTemplateResponse;
import konkuk.chacall.global.common.dto.BaseResponse;
import konkuk.chacall.global.common.swagger.SwaggerResponseDescription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Owner API", description = "사장님 관련 API")

@RestController
@RequiredArgsConstructor
@RequestMapping("/owners")
@Slf4j
public class OwnerController {

    private final OwnerService ownerService;

    @Operation(
            summary = "은행 계좌 등록",
            description = "사장님이 자신의 은행 계좌를 등록합니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.OWNER_REGISTER_BANK_ACCOUNT)
    @PostMapping("/me/bank-accounts")
    public BaseResponse<Void> registerBankAccount(
            @RequestBody @Valid final RegisterBankAccountRequest registerBankAccountRequest,
            @Parameter(hidden = true) @UserId final Long ownerId
    ) {
        ownerService.registerBankAccount(registerBankAccountRequest, ownerId);

        return BaseResponse.ok(null);
    }

    @Operation(
            summary = "은행 계좌 조회",
            description = "사장님이 자신의 은행 계좌를 조회합니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.OWNER_GET_BANK_ACCOUNT)
    @GetMapping("/me/bank-accounts")
    public BaseResponse<BankAccountResponse> getBankAccount(
            @Parameter(hidden = true) @UserId final Long ownerId
    ) {
        return BaseResponse.ok(ownerService.getBankAccount(ownerId));
    }

    @Operation(
            summary = "은행 계좌 수정",
            description = "사장님이 자신의 은행 계좌를 수정합니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.OWNER_UPDATE_BANK_ACCOUNT)
    @PatchMapping("/me/bank-accounts/{bankAccountId}")
    public BaseResponse<Void> updateBankAccount(
            @PathVariable final Long bankAccountId,
            @RequestBody @Valid final UpdateBankAccountRequest request,
            @Parameter(hidden = true) @UserId final Long ownerId
            ) {
        ownerService.updateBankAccount(ownerId, bankAccountId, request);
        return BaseResponse.ok(null);
    }

    @Operation(
            summary = "은행 계좌 삭제",
            description = "사장님이 자신의 은행 계좌를 삭제합니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.OWNER_DELETE_BANK_ACCOUNT)
    @DeleteMapping("/me/bank-accounts/{bankAccountId}")
    public BaseResponse<Void> deleteBankAccount(
            @PathVariable final Long bankAccountId,
            @Parameter(hidden = true) @UserId final Long ownerId
    ) {
        ownerService.deleteBankAccount(ownerId, bankAccountId);
        return BaseResponse.ok(null);
    }


    @PostMapping("/me/chat-templates")
    public BaseResponse<Void> registerChatTemplate(
            @RequestBody @Valid RegisterChatTemplateRequest request
    ) {
        // todo 추후에 토큰 추가될 시 id 값은 토큰에서 추출하여 전달
        ownerService.registerChatTemplate(request, 1L);

        return BaseResponse.ok(null);
    }

    @GetMapping("/me/chat-templates")
    public BaseResponse<List<ChatTemplateResponse>> getChatTemplates() {
        // todo 추후에 토큰 추가될 시 id 값은 토큰에서 추출하여 전달
        return BaseResponse.ok(ownerService.getChatTemplates(1L));
    }

    @PatchMapping("/me/chat-templates/{chatTemplateId}")
    public BaseResponse<Void> updateChatTemplate(
            @PathVariable Long chatTemplateId,
            @RequestBody @Valid UpdateChatTemplateRequest request) {
        // todo 추후에 토큰 추가될 시 id 값은 토큰에서 추출하여 전달
        ownerService.updateChatTemplate(request, 1L, chatTemplateId);
        return BaseResponse.ok(null);
    }

    @DeleteMapping("/me/chat-templates/{chatTemplateId}")
    public BaseResponse<Void> deleteChatTemplate(
            @PathVariable Long chatTemplateId) {
        // todo 추후에 토큰 추가될 시 id 값은 토큰에서 추출하여 전달
        ownerService.deleteChatTemplate(1L, chatTemplateId);
        return BaseResponse.ok(null);
    }
}
