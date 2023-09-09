package ui.edit

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import data.database.entities.Account
import data.database.entities.Money
import data.enums.AccountType
import ui.components.BottomSheetFromWish
import ui.components.CancelAction
import ui.components.CommonAppBar
import ui.components.DatePicker
import ui.theme.*
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun EditAccountScreen(
    account: Account? = null,
    isOpen: Boolean,
    onSuccess: (Account?) -> Unit,
    onCancel: () -> Unit,
) {
    BottomSheetFromWish(
        visible = isOpen
    ) {
        val vm = EditAccountViewModel()
        val uiState by vm.uiState.collectAsState()
        val title = if (account == null) "Create account"
        else "Update account"
        account?.let {
            LaunchedEffect(it) {
                vm.updateAccountData(it)
            }
        }
        Scaffold(
            topBar = {
                CommonAppBar(
                    title = title,
                    actions = {
                        CancelAction(onCancel)
                    }
                )
            }
        ) { padding ->
            EditAccountBody(
                paddingValues = padding,
                uiState = uiState,
                intent = vm,
                onSuccess = onSuccess,
                isCreating = account == null
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditAccountBody(
    paddingValues: PaddingValues,
    uiState: EditAccountViewModel.UiState,
    intent: EditIntent,
    onSuccess: (Account?) -> Unit,
    isCreating: Boolean = true,
) {
    val buttonConfirmText = if (isCreating) "Create Account"
    else "Update Account"

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(all = PADDING_BIG)
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(PADDING_BIG)
    ) {

        OutlinedTextField(
            value = uiState.name,
            onValueChange = {
                intent.updateUserName(it)
            },
            label = {
                Text("Name")
            },
            isError = uiState.nameError != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        uiState.nameError?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }

        OutlinedTextField(
            value = uiState.startBalance,
            onValueChange = {
                intent.updateStartBalance(it)
            },
            isError = uiState.startDateError != null,
            label = {
                Text("Current Balance")
            },
            placeholder = {
                Text("Balance format: 1.23")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        uiState.startBalanceError?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }

        Box {
            OutlinedTextField(
                readOnly = true,
                value = formatDate(uiState.startDate),
                onValueChange = {},
                isError = uiState.startDateError != null,
                label = { Text("Date of current balance") },
                trailingIcon = {
                    Icon(
                        Icons.Default.DateRange, contentDescription = "dateIcon"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0f)
                    .clickable(onClick = {
                        intent.openDateDialog()
                    }),
            )
        }

        AccountTypeDropdown(
            expanded = uiState.accountTypeMenuExpanded,
            selectedItem = uiState.accountType,
            onExpandedChange = {
                intent.updateAccountTypeExpanded(!it)
            },
            onDismissRequest = {
                intent.updateAccountTypeExpanded(false)
            },
            onSelectType = {
                intent.updateAccountType(it)
            },
            isError = uiState.accountTypeError != null
        )
        uiState.accountTypeError?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }

        Column(
            modifier = Modifier
                .selectableGroup()
                .padding(bottom = PADDING_BIG),
            verticalArrangement = Arrangement.spacedBy(PADDING_MED)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(PADDING_SMALL),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        intent.updateBudgetType(EditAccountViewModel.BudgetType.BudgetAccount)
                    }
            ) {
                RadioButton(
                    selected = uiState.isBudgetAccount ?: false,
                    onClick = {
                        intent.updateBudgetType(EditAccountViewModel.BudgetType.BudgetAccount)
                    },
                    modifier =
                    Modifier.size(CHECKBOX_SIZE_SMALL)
                )
                Column {
                    Text(
                        text = EditAccountViewModel.BudgetType.BudgetAccount.type,
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = EditAccountViewModel.BudgetType.BudgetAccount.desc,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(PADDING_SMALL),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        intent.updateBudgetType(EditAccountViewModel.BudgetType.OffBudget)
                    }
            ) {
                RadioButton(
                    selected = uiState.isBudgetAccount != null && !uiState.isBudgetAccount,
                    onClick = {
                        intent.updateBudgetType(EditAccountViewModel.BudgetType.OffBudget)
                    },
                    modifier =
                    Modifier.size(CHECKBOX_SIZE_SMALL)
                )
                Column {
                    Text(
                        text = EditAccountViewModel.BudgetType.OffBudget.type, style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = EditAccountViewModel.BudgetType.OffBudget.desc,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
            uiState.isBudgetAccountError?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                intent.submitAccount {
                    if (it) {
                        onSuccess(
                            Account(
                                id = uiState.accountId,
                                name = uiState.name,
                                startDate = uiState.startDate ?: Date(),
                                startBalance = Money(amount = uiState.startBalance.toDoubleOrNull() ?: -1.0),
                                isBudgetAccount = uiState.isBudgetAccount ?: false,
                                accountType = uiState.accountType
                            )
                        )
                    }


                }
            }
        ) {
            Text(buttonConfirmText)
        }

        if (!isCreating)
            Button(
                onClick = {
                    intent.openDeleteDialog()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Delete account")
            }
    }

    if (uiState.deleteDialogOpened)
        AlertDialog(
            title = {
                Text(
                    text = "Confirm action",
                    color = MaterialTheme.colors.onBackground
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete the account?",
                    color = MaterialTheme.colors.onBackground
                )
            },
            buttons =
            {
                DialogButtons(
                    onConfirm = { onSuccess(null) },
                    onDismissRequest = {
                        intent.closeDeleteDialog()
                    },
                    confirmButtonText = "Delete",
                    dismissButtonText = "No",
                    confirmButtonColors = ButtonDefaults
                        .buttonColors(backgroundColor = MaterialTheme.colors.error)
                )
            },
            onDismissRequest = {
                intent.closeDeleteDialog()
            },
            backgroundColor = MaterialTheme.colors.background
        )


    if (uiState.dateDialogOpened)
        DatePicker(
            initDate = Date(),
            onDismissRequest = {
                intent.closeDateDialog()
            },
            onDateSelect = {
                intent.updateStartDate(it)
                intent.closeDateDialog()
            }
        )

}


@Composable
private fun DialogButtons(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    confirmButtonText: String,
    dismissButtonText: String,
    confirmButtonColors: ButtonColors = ButtonDefaults.buttonColors(),
    dismissButtonColors: ButtonColors = ButtonDefaults.buttonColors(),
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(SPACE_MED),
        modifier = Modifier
            .fillMaxWidth()
            .padding(PADDING_BIG)
    ) {
        Button(
            onClick = onConfirm,
            colors = confirmButtonColors,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                confirmButtonText,
                style = MaterialTheme.typography.body1
            )
        }
        Button(
            onClick = onDismissRequest,
            colors = dismissButtonColors,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                dismissButtonText,
                style = MaterialTheme.typography.body1
            )
        }
    }
}


@Composable
fun AccountTypeDropdown(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    onSelectType: (AccountType) -> Unit,
    selectedItem: AccountType,
    isError: Boolean,
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CORNER_RADIUS_SMALL))
            .border(
                BorderStroke(
                    BORDER_SIZE_SMALL, color = if (isError) MaterialTheme.colors.error
                    else MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
                ), RoundedCornerShape(CORNER_RADIUS_SMALL)
            )
            .padding(PADDING_MED)
            .clickable(onClick = { onExpandedChange(expanded) }),
    ) {
        Text(
            text = selectedItem.type,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(PADDING_MED)
        )
        Icon(
            Icons.Filled.ArrowDropDown, "contentDescription",
            Modifier.align(Alignment.CenterEnd)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            AccountType.values().forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onSelectType(selectionOption)
                    }
                ) {
                    Text(text = selectionOption.type)
                }
            }
        }
    }
}

private fun formatDate(date: Date?): String {
    if (date == null) return ""
    val dateFormat = SimpleDateFormat("d.M.y")
    return dateFormat.format(date)
}

@Preview
@Composable
fun PrevEdit() {
    MaterialTheme {
        //FIXME: Не заработало превью
        EditAccountBody(
            uiState = EditAccountViewModel.UiState(),
            intent = EditAccountViewModel(),
            onSuccess = {},
            paddingValues = PaddingValues()
        )
    }
}