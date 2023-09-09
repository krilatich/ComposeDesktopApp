package ui.menu

import SpringJpaComposeApp
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import ui.list.AccountsListScreen
import ui.list.enums.AccountsListMode
import ui.theme.PADDING_BIG
import ui.theme.SPACE_MED

@Composable
fun MenuScreen(
    context: ConfigurableApplicationContext,
    uiState: MenuViewModel.UiState,
    updateActiveAccount: (accountId: Int) -> Unit,
    openAccountsList: (mode: AccountsListMode) -> Unit,
    closeAccountsList: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
            .padding(PADDING_BIG),
        verticalArrangement = Arrangement.spacedBy(
            space = SPACE_MED,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MENU",
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h2
        )
        Button(
            onClick = { openAccountsList(AccountsListMode.Editing) },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Accounts")
        }
        Button(
            onClick = { openAccountsList(AccountsListMode.Choice) },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Choice an account")
        }
        Text(
            text = if (uiState.account == null) "No active account"
            else "Active account: " + uiState.account.name,
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.body1
        )
    }
    if (uiState.listMode != null)
        AccountsListScreen(
            context = context,
            mode = uiState.listMode,
            onCancel = closeAccountsList,
            onChoice = {
                updateActiveAccount(it)
                closeAccountsList()
            }
        )
}

@Preview
@Composable
fun MenuPrev() {
    MaterialTheme() {
        MenuScreen(
            context = runApplication<SpringJpaComposeApp>(),
            uiState = MenuViewModel.UiState(),
            updateActiveAccount = {},
            closeAccountsList = {},
            openAccountsList = {}
        )
    }
}