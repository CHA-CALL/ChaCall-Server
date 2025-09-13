package konkuk.chacall.domain.owner.domain.model;

import jakarta.persistence.*;
import konkuk.chacall.domain.user.domain.model.User;
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

    @Column(length = 30, nullable = false)
    private String accountNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    public void update(String bankName, String accountHolderName, String accountNumber) {
        this.bankName = bankName;
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
    }
}
