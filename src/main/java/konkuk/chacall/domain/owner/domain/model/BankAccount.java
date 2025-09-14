package konkuk.chacall.domain.owner.domain.model;

import jakarta.persistence.*;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.DomainRuleException;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.*;

@Entity
@Table(name = "bank_accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bankAccountId;

    @Column(length = 20, nullable = false)
    private String bankName;

    @Column(length = 20, nullable = false)
    private String accountHolderName;

    @Column(length = 30, nullable = false, unique = true)
    private String accountNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User owner;

    public static BankAccount of(String bankName, String accountHolderName, String accountNumber, User owner) {
        return BankAccount.builder()
                .bankName(bankName)
                .accountHolderName(accountHolderName)
                .accountNumber(accountNumber)
                .owner(owner)
                .build();
    }

    public void update(String bankName, String accountHolderName, String accountNumber) {
        this.bankName = bankName;
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
    }

    public void verifyOwner(Long ownerId) {
        if (!this.owner.getUserId().equals(ownerId)) {
            throw new DomainRuleException(ErrorCode.BANK_ACCOUNT_FORBIDDEN);
        }
    }

}
