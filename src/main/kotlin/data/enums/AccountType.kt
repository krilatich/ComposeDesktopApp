package data.enums

/**
 * Тип счёта.
 * Сейчас это не особо влияет на логику, но в дальнейшем позволит избирательно подключать функционал.
 */
enum class AccountType (val id: Int?, val type: String) {
    NONE(
        id = null,
        type = "Укажите тип счёта"
    ),
    CASH(
        id = 1,
        type = "Наличные"
    ),
    CARD_DEBIT(
        id = 2,
        type = "Карта дебетовая"
    ),
    CARD_CREDIT(
        id = 3,
        type = "Карта кредитная"
    ),
    CREDIT(
        id = 4,
        type = "Кредит"
    ),
    MORTGAGE(
        id = 5,
        type = "Ипотека"
    ),
    INVESTMENT(
        id = 6,
        type = "Инвестиционный"
    ),
    SAVINGS(
        id = 7,
        type = "Накопительный"
    ),
    DEBTS(
        id = 8,
        type = "Долги"
    ),
    OTHER(
        id = 99,
        type = "Другое"
    )
}