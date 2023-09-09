package ui.list

import data.database.entities.Account

interface AccountsIntent {
    fun dismissEdit()
    fun onEditClicked(account: Account?)
    fun createAccount(account: Account)
    fun updateAccount(account: Account)
    fun deleteAccount(accountId: Int)
}