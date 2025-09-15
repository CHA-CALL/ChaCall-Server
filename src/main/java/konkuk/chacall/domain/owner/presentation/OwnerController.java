package konkuk.chacall.domain.owner.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.chacall.domain.owner.application.OwnerService;
import konkuk.chacall.domain.owner.presentation.dto.request.*;
import konkuk.chacall.domain.owner.presentation.dto.response.BankAccountResponse;
import konkuk.chacall.domain.owner.presentation.dto.response.OwnerReservationDetailResponse;
import konkuk.chacall.domain.owner.presentation.dto.response.OwnerReservationHistoryResponse;
import konkuk.chacall.global.common.annotation.ExceptionDescription;
import konkuk.chacall.global.common.annotation.UserId;
import konkuk.chacall.domain.owner.presentation.dto.response.ChatTemplateResponse;
import konkuk.chacall.global.common.dto.BaseResponse;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.swagger.SwaggerResponseDescription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
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


    @Operation(
            summary = "자주 쓰는 채팅 등록",
            description = "사장님이 자주 쓰는 채팅을 등록합니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.OWNER_REGISTER_CHAT_TEMPLATE)
    @PostMapping("/me/chat-templates")
    public BaseResponse<Void> registerChatTemplate(
            @RequestBody @Valid final RegisterChatTemplateRequest request,
            @Parameter(hidden = true) @UserId final Long ownerId
    ) {
        ownerService.registerChatTemplate(request, ownerId);

        return BaseResponse.ok(null);
    }

    @Operation(
            summary = "자주 쓰는 채팅 조회",
            description = "사장님이 자주 쓰는 채팅을 조회합니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.OWNER_GET_CHAT_TEMPLATE)
    @GetMapping("/me/chat-templates")
    public BaseResponse<List<ChatTemplateResponse>> getChatTemplates(
            @Parameter(hidden = true) @UserId final Long ownerId
    ) {
        return BaseResponse.ok(ownerService.getChatTemplates(ownerId));
    }

    @Operation(
            summary = "자주 쓰는 채팅 수정",
            description = "사장님이 자주 쓰는 채팅을 수정합니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.OWNER_UPDATE_CHAT_TEMPLATE)
    @PatchMapping("/me/chat-templates/{chatTemplateId}")
    public BaseResponse<Void> updateChatTemplate(
            @PathVariable final Long chatTemplateId,
            @RequestBody @Valid final UpdateChatTemplateRequest request,
            @Parameter(hidden = true) @UserId final Long ownerId
    ) {
        ownerService.updateChatTemplate(request, ownerId, chatTemplateId);
        return BaseResponse.ok(null);
    }

    @Operation(
            summary = "자주 쓰는 채팅 삭제",
            description = "사장님이 자주 쓰는 채팅을 삭제합니다."
    )
    @ExceptionDescription(SwaggerResponseDescription.OWNER_DELETE_CHAT_TEMPLATE)
    @DeleteMapping("/me/chat-templates/{chatTemplateId}")
    public BaseResponse<Void> deleteChatTemplate(
            @PathVariable final Long chatTemplateId,
            @Parameter(hidden = true) @UserId final Long ownerId) {
        ownerService.deleteChatTemplate(ownerId, chatTemplateId);
        return BaseResponse.ok(null);
    }

    @Operation(
            summary = "사장님 예약 내역 목록 조회 (무한 스크롤)",
            description = "사장님의 예약 내역 목록을 조회합니다.")
    @GetMapping("/me/reservations")
    public BaseResponse<CursorPagingResponse<OwnerReservationHistoryResponse>> getOwnerReservations(
            @Valid @ParameterObject final GetReservationHistoryRequest request,
            @Parameter(hidden = true) @UserId final Long ownerId
    ) {
        return BaseResponse.ok(ownerService.getOwnerReservations(
                request,
                ownerId));
    }

    @Operation(
            summary = "예약 상세 조회",
            description = "예약 ID로 예약 상세 정보를 조회합니다.")
    @GetMapping("me/reservations/{reservationId}")
    public BaseResponse<OwnerReservationDetailResponse> getReservationDetail(
            @PathVariable final Long reservationId,
            @Parameter(hidden = true) @UserId final Long ownerId
    ) {
        return BaseResponse.ok(ownerService.getReservationDetail(
                ownerId,
                reservationId));
    }
}
