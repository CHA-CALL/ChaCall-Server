package konkuk.chacall.domain.owner.domain.repository;

import konkuk.chacall.domain.owner.domain.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    boolean existsByOwner_UserIdAndAccountNumber(Long userId, String accountNumber);
}
