package ui.edit

import data.database.entities.Account
import data.enums.AccountType
import java.util.*

interface EditIntent {
    fun openDeleteDialog()
    fun closeDeleteDialog()
    fun openDateDialog()
    fun closeDateDialog()
    fun updateAccountData(account: Account)
    fun updateUserName(name: String)
    fun updateStartBalance(balance: String)
    fun updateStartDate(date: Date)
    fun updateAccountType(type: AccountType)
    fun updateBudgetType(budget: EditAccountViewModel.BudgetType)
    fun updateAccountTypeExpanded(isExpanded: Boolean)
    fun submitAccount(onValidated: (Boolean) -> Unit)
}