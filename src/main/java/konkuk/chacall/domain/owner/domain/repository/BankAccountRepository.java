package konkuk.chacall.domain.owner.domain.repository;

import konkuk.chacall.domain.owner.domain.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByOwner_UserId(Long userId);
}
