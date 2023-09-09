package ui.list

import SpringJpaComposeApp
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import data.database.entities.Account
import org.springframework.beans.factory.getBean
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import ui.components.CommonAppBar
import ui.edit.EditAccountScreen
import ui.list.enums.AccountsListMode
import ui.theme.BUTTON_HEIGHT_BIG
import ui.theme.PADDING_BIG
import ui.theme.PADDING_MED
import ui.theme.PADDING_SMALL


@Composable
fun AccountsListScreen(
    context: ConfigurableApplicationContext,
    mode: AccountsListMode,
    onCancel: () -> Unit,
    onChoice: (accountId: Int) -> Unit,
) {
    val vm = context.getBean<AccountsListViewModel>()
    val uiState by vm.uiState.collectAsState()
    val title = if (mode == AccountsListMode.Editing) "Editing"
    else "Choice"

    Scaffold(
        topBar = {
            CommonAppBar(title = title, onBackClick = onCancel)
        },
        floatingActionButton = {
            if (mode == AccountsListMode.Editing)
                FloatingActionButton(onClick = { vm.onEditClicked(null) }) {
                    Icon(
                        Icons.Default.Add, contentDescription = "addIcon"
                    )
                }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        when {
            uiState.isLoading -> LoadingScreen()
            uiState.accounts.isEmpty() ->
                when (mode) {
                    AccountsListMode.Editing -> EmptyListScreenEditable()

                    AccountsListMode.Choice -> EmptyListScreen()
                }

            else -> AccountsListBody(
                padding = padding,
                uiState = uiState,
                onAccountClick = {
                    when (mode) {
                        AccountsListMode.Editing -> vm.onEditClicked(it)

                        AccountsListMode.Choice -> onChoice(it.id)
                    }
                }
            )
        }
    }
    EditAccountScreen(
        account = uiState.editingAccount,
        isOpen = uiState.editAccountIsOpen,
        onCancel = { vm.dismissEdit() },
        onSuccess = { acc ->
            // Закрываем окно
            vm.dismissEdit()
            if (acc == null) {
                // Результат null -> удаляем выбранный счет
                uiState.editingAccount?.let {
                    vm.deleteAccount(it.id)
                }
            } else if (uiState.editingAccount == null) {
                // не выбирали существующий -> новый счет
                vm.createAccount(acc)
            } else
            // обновляем выбранный счет
                vm.updateAccount(acc)
        },
    )
}

@Composable
fun AccountsListBody(
    padding: PaddingValues,
    uiState: AccountsListViewModel.UiState,
    onAccountClick: (Account) -> Unit,
) {
    LazyColumn(
        Modifier
            .padding(padding)
            .padding(vertical = PADDING_MED, horizontal = PADDING_BIG)
            .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(PADDING_SMALL)
    ) {
        items(uiState.accounts.size) {
            AccountItem(account = uiState.accounts[it],
                onClick = { onAccountClick(uiState.accounts[it]) })
        }
    }
}


@Composable
fun AccountItem(
    account: Account, onClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(BUTTON_HEIGHT_BIG),
        onClick = onClick,
    ) {
        Text(
            text = account.name, style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun EmptyListScreen() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        Text(
            text = "No accounts yet 🙁",
            style = MaterialTheme.typography.h3,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun EmptyListScreenEditable() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        Text(
            text = "No accounts\nPress + button to create",
            style = MaterialTheme.typography.h3,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Preview
@Composable
fun EditingList() {
    MaterialTheme {
        AccountsListScreen(
            context = runApplication<SpringJpaComposeApp> {},
            mode = AccountsListMode.Editing,
            onCancel = {},
            onChoice = {}
        )
    }

}

@Preview
@Composable
fun ChoiceList() {
    MaterialTheme {
        AccountsListScreen(
            context = runApplication<SpringJpaComposeApp> {},
            mode = AccountsListMode.Choice,
            onCancel = {},
            onChoice = {}
        )
    }
}