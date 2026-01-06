package konkuk.chacall.domain.owner.application.bankaccount;

import konkuk.chacall.domain.owner.domain.model.BankAccount;
import konkuk.chacall.domain.owner.domain.repository.BankAccountRepository;
import konkuk.chacall.domain.owner.presentation.dto.request.RegisterBankAccountRequest;
import konkuk.chacall.domain.owner.presentation.dto.request.UpdateBankAccountRequest;
import konkuk.chacall.domain.owner.presentation.dto.response.BankAccountResponse;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.AuthException;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BankAccountService 테스트")
class BankAccountServiceTest {

    @InjectMocks
    private BankAccountService bankAccountService;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private User owner;

    @Mock
    private BankAccount bankAccount;

    @Nested
    @DisplayName("은행 계좌 등록 시나리오")
    class RegisterBankAccountScenario {
        private final RegisterBankAccountRequest request = new RegisterBankAccountRequest("은행", "예금주", "12345");

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("계좌가 존재하지 않을 경우 성공적으로 등록한다")
            void registerNewBankAccountSuccess() {
                // given
                given(owner.getUserId()).willReturn(1L);
                given(bankAccountRepository.existsByOwner_UserId(anyLong())).willReturn(false);
                given(bankAccountRepository.existsByAccountNumber(anyString())).willReturn(false);
                given(bankAccountRepository.save(any(BankAccount.class))).willReturn(bankAccount);

                // when
                bankAccountService.registerBankAccount(request, owner);

                // then
                verify(bankAccountRepository).save(any(BankAccount.class));
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("해당 사용자의 계좌가 이미 존재할 경우 BANK_ACCOUNT_ALREADY_EXISTS_FOR_USER 예외를 발생시킨다")
            void registerWhenUserAccountExistsFail() {
                // given
                given(owner.getUserId()).willReturn(1L);
                given(bankAccountRepository.existsByOwner_UserId(anyLong())).willReturn(true);

                // when
                BusinessException ex = assertThrows(BusinessException.class,
                        () -> bankAccountService.registerBankAccount(request, owner));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.BANK_ACCOUNT_ALREADY_EXISTS_FOR_USER);
            }

            @Test
            @DisplayName("동일한 계좌번호가 시스템에 이미 존재할 경우 BANK_ACCOUNT_ALREADY_EXISTS 예외를 발생시킨다")
            void registerWhenAccountNumberExistsFail() {
                // given
                given(owner.getUserId()).willReturn(1L);
                given(bankAccountRepository.existsByOwner_UserId(anyLong())).willReturn(false);
                given(bankAccountRepository.existsByAccountNumber(anyString())).willReturn(true);

                // when
                BusinessException ex = assertThrows(BusinessException.class,
                        () -> bankAccountService.registerBankAccount(request, owner));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.BANK_ACCOUNT_ALREADY_EXISTS);
            }
        }
    }

    @Nested
    @DisplayName("은행 계좌 조회 시나리오")
    class GetBankAccountScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("계좌가 존재할 경우 계좌 정보를 반환한다")
            void getExistingBankAccountSuccess() {
                // given
                given(bankAccountRepository.findByOwnerId(anyLong())).willReturn(Optional.of(bankAccount));

                // when
                BankAccountResponse response = bankAccountService.getBankAccount(1L);

                // then
                assertThat(response).isNotNull();
            }

            @Test
            @DisplayName("계좌가 존재하지 않을 경우 null을 반환한다")
            void getNonExistingBankAccountReturnsNull() {
                // given
                given(bankAccountRepository.findByOwnerId(anyLong())).willReturn(Optional.empty());

                // when
                BankAccountResponse response = bankAccountService.getBankAccount(1L);

                // then
                assertNull(response);
            }
        }
    }

    @Nested
    @DisplayName("은행 계좌 수정 시나리오")
    class UpdateBankAccountScenario {
        private final UpdateBankAccountRequest request = new UpdateBankAccountRequest("새은행", "새예금주", "54321");

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("소유주가 자신의 계좌 정보를 성공적으로 수정한다")
            void updateBankAccountByOwnerSuccess() {
                // given
                given(bankAccountRepository.findById(anyLong())).willReturn(Optional.of(bankAccount));
                doNothing().when(bankAccount).verifyOwner(anyLong());
                given(bankAccount.getAccountNumber()).willReturn("12345");
                given(bankAccountRepository.existsByAccountNumber(anyString())).willReturn(false);
                doNothing().when(bankAccount).update(anyString(), anyString(), anyString());

                // when
                bankAccountService.updateBankAccount(1L, 1L, request);

                // then
                verify(bankAccount).update(anyString(), anyString(), anyString());
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 계좌 ID로 요청 시 BANK_ACCOUNT_NOT_FOUND 예외를 발생시킨다")
            void updateWithInvalidAccountIdFail() {
                // given
                given(bankAccountRepository.findById(anyLong())).willReturn(Optional.empty());

                // when
                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                        () -> bankAccountService.updateBankAccount(1L, 99L, request));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.BANK_ACCOUNT_NOT_FOUND);
            }

            @Test
            @DisplayName("소유주가 아닌 사용자가 요청 시 AUTH_FORBIDDEN 예외를 발생시킨다")
            void updateByNotOwnerFail() {
                // given
                given(bankAccountRepository.findById(anyLong())).willReturn(Optional.of(bankAccount));
                doThrow(new AuthException(ErrorCode.USER_FORBIDDEN)).when(bankAccount).verifyOwner(anyLong());

                // when
                AuthException ex = assertThrows(AuthException.class,
                        () -> bankAccountService.updateBankAccount(2L, 1L, request));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_FORBIDDEN);
            }
        }
    }

    @Nested
    @DisplayName("은행 계좌 삭제 시나리오")
    class DeleteBankAccountScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("소유주가 자신의 계좌를 성공적으로 삭제한다")
            void deleteBankAccountByOwnerSuccess() {
                // given
                given(bankAccountRepository.findById(anyLong())).willReturn(Optional.of(bankAccount));
                doNothing().when(bankAccount).verifyOwner(anyLong());
                doNothing().when(bankAccountRepository).delete(any(BankAccount.class));

                // when
                bankAccountService.deleteBankAccount(1L, 1L);

                // then
                verify(bankAccountRepository).delete(bankAccount);
            }
        }
    }
}
