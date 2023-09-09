package ui.menu


import base.BaseViewModel
import data.database.entities.Account
import data.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import ui.list.enums.AccountsListMode

@Component
class MenuViewModel(
    private val repository: AccountRepository,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun updateActiveAccount(accountId: Int) = viewModelScope.launch {
        _uiState.update {
            it.copy(
                account = getAccountById(accountId)
            )
        }
    }

    fun openAccountList(mode: AccountsListMode) {
        _uiState.update {
            it.copy(
                listMode = mode
            )
        }
    }

    fun closeAccountsList() {
        _uiState.update {
            it.copy(
                listMode = null,
                account = it.account?.id?.let { accId -> getAccountById(accId) }
            )
        }
    }

    private fun getAccountById(id: Int) = repository.findByIdOrNull(id)

    data class UiState(
        val account: Account? = null,
        val listMode: AccountsListMode? = null,
    )

}