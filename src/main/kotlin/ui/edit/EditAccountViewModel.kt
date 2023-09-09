package ui.edit

import base.BaseViewModel
import data.database.entities.Account
import data.enums.AccountType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

class EditAccountViewModel : BaseViewModel(), EditIntent {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()


    override fun openDeleteDialog() {
        _uiState.update {
            it.copy(
                deleteDialogOpened = true
            )
        }
    }

    override fun closeDeleteDialog() {
        _uiState.update {
            it.copy(
                deleteDialogOpened = false
            )
        }
    }

    override fun openDateDialog() {
        _uiState.update {
            it.copy(
                dateDialogOpened = true
            )
        }
    }

    override fun closeDateDialog() {
        _uiState.update {
            it.copy(
                dateDialogOpened = false
            )
        }
    }

    override fun updateAccountData(account: Account) {
        _uiState.update {
            it.copy(
                accountId = account.id,
                name = account.name,
                startDate = account.startDate,
                startBalance = account.startBalance.amount.toString(),
                isBudgetAccount = account.isBudgetAccount,
                accountType = account.accountType
            )
        }
    }

    override fun updateUserName(name: String) {
        _uiState.update {
            it.copy(
                name = name,
                nameError = validateName(name)
            )
        }
    }

    override fun updateStartBalance(balance: String) {
        _uiState.update {
            it.copy(
                startBalance = balance,
                startBalanceError = validateStartBalance(balance)
            )
        }
    }

    override fun updateStartDate(date: Date) {
        _uiState.update {
            it.copy(
                startDate = date,
                startDateError = validateDate(date)
            )
        }
    }

    override fun updateAccountType(type: AccountType) {
        _uiState.update {
            it.copy(
                accountTypeMenuExpanded = false,
                accountType = type,
                accountTypeError = validateAccountType(type)
            )
        }
    }

    override fun updateBudgetType(budgetType: BudgetType) {
        _uiState.update {
            it.copy(
                isBudgetAccount = budgetType == BudgetType.BudgetAccount
            )
        }
    }

    override fun updateAccountTypeExpanded(isExpanded: Boolean) {
        _uiState.update {
            it.copy(
                accountTypeMenuExpanded = isExpanded
            )
        }
    }

    override fun submitAccount(onValidated: (Boolean) -> Unit) {
        // Валидация всех полей
        _uiState.update {
            it.copy(
                accountTypeError = validateAccountType(uiState.value.accountType),
                isBudgetAccountError = validateBudgetType(uiState.value.isBudgetAccount),
                nameError = validateName(uiState.value.name),
                startDateError = validateDate(uiState.value.startDate),
                startBalanceError = validateStartBalance(uiState.value.startBalance),
            )
        }
        val noErrors: Boolean =
            with(uiState.value) {
                (nameError == null
                        && startBalanceError == null
                        && startDateError == null
                        && accountTypeError == null
                        && isBudgetAccountError == null)
            }
        // Отправляем наличие ошибок
        onValidated(noErrors)
    }

    data class UiState(
        val accountId: Int = 0,
        val name: String = "",
        val startBalance: String = "",
        val startDate: Date? = null,
        val isBudgetAccount: Boolean? = null,
        val accountType: AccountType = AccountType.NONE,
        val deleteDialogOpened: Boolean = false,
        val accountTypeMenuExpanded: Boolean = false,
        val dateDialogOpened: Boolean = false,
        val nameError: String? = null,
        val startBalanceError: String? = null,
        val startDateError: String? = null,
        val isBudgetAccountError: String? = null,
        val accountTypeError: String? = null,
    )

    enum class BudgetType(val type: String, val desc: String) {
        BudgetAccount(
            type = "Budget Account",
            desc = "This account should affect my budget"
        ),
        OffBudget(
            type = "Off-Budget",
            desc = "This account should not affect my budget"
        )
    }

    // Валидация полей
    private fun validateName(name: String): String? {
        return when {
            name.isEmpty() -> "empty name"
            name.length < 5 -> "name too short"
            else -> null
        }
    }

    private fun validateStartBalance(balance: String): String? {
        return when {
            balance.isEmpty() -> "empty balance"
            balance.toDoubleOrNull() == null -> "Balance format: 1.23"
            (balance.toDoubleOrNull() ?: 0.0) < 0.0 -> "balance must be greater then zero"
            else -> null
        }
    }


    private fun validateDate(date: Date?): String? {
        return if (date == null) "empty date" else null
    }

    private fun validateAccountType(type: AccountType): String? {
        return if (type == AccountType.NONE) "account not selected" else null
    }

    private fun validateBudgetType(isBudgetAccount: Boolean?): String? {
        return if (isBudgetAccount == null) "select budget affect" else null
    }
}

