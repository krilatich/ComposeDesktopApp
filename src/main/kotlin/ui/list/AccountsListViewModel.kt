package ui.list

import base.BaseViewModel
import data.database.entities.Account
import data.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class AccountsListViewModel
    (
    private val repository: AccountRepository,
) : BaseViewModel(), AccountsIntent {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()


    init {
        startLoading()
        getAccounts()
        endLoading()
    }

    private fun getAccounts() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    accounts = getAllDb()
                )
            }
        }
    }

    override fun createAccount(account: Account) {
        viewModelScope.launch {
            startLoading()
            createDb(account)
            getAccounts()
            endLoading()
        }
    }

    override fun deleteAccount(accountId: Int) {
        viewModelScope.launch {
            startLoading()
            deleteDb(accountId)
            getAccounts()
            endLoading()
        }
    }

    override fun updateAccount(account: Account) {
        viewModelScope.launch {
            startLoading()
            updateDb(account)
            getAccounts()
            endLoading()
        }
    }

    override fun onEditClicked(account: Account?) {
        _uiState.update {
            it.copy(
                editAccountIsOpen = true,
                editingAccount = account
            )
        }
    }

    override fun dismissEdit() {
        _uiState.update {
            it.copy(editAccountIsOpen = false)
        }
    }


    data class UiState(
        val editAccountIsOpen: Boolean = false,
        val accounts: List<Account> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val editingAccount: Account? = null,
    )

    private fun startLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    private fun endLoading() {
        _uiState.update { it.copy(isLoading = false) }
    }

    private fun getAllDb(): List<Account> =
        repository.findAll()

    private fun createDb(account: Account) =
        repository.save(account)

    private fun updateDb(account: Account): Boolean {
        if (repository.findByIdOrNull(account.id) == null)
            return false
        repository.save(account)
        return true
    }

    private fun deleteDb(accountId: Int): Boolean {
        if (repository.findByIdOrNull(accountId) == null)
            return false
        repository.deleteById(accountId)
        return true
    }
}