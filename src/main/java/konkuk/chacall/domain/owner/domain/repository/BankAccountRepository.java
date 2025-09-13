package konkuk.chacall.domain.owner.domain.repository;

import konkuk.chacall.domain.owner.domain.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    // 계좌 번호가 DB 상에 이미 존재하는지 여부 조회
    boolean existsByAccountNumber(String accountNumber);

    // 사장님 ID 기반 계좌 등록 여부 조회
    boolean existsByOwner_UserId(Long userId);

    // 사장님 ID 기반 계좌 조회
    Optional<BankAccount> findByOwner_UserId(Long userId);

    // 계좌 ID와 사장님 ID로 계좌 조회
    Optional<BankAccount> findByBankAccountIdAndOwner_UserId(Long bankAccountId, Long ownerId);
}
