package data.database.entities

import javax.persistence.*

@Entity
@Table(name = "money")
data class Money(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val amount: Double,
)
