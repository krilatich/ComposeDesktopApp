package data.database.entities

import data.enums.AccountType
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import java.util.*
import javax.persistence.*

// URL: https://jpa-buddy.com/blog/best-practices-and-common-pitfalls/
@Entity
@Table(name = "account")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val name: String,
    @OneToOne
    @JoinColumn(name = "money_id")
    @Cascade(CascadeType.ALL)
    val startBalance: Money,
    val startDate: Date,
    val isBudgetAccount: Boolean,
    val accountType: AccountType,
    val isClosed: Boolean = false,
)