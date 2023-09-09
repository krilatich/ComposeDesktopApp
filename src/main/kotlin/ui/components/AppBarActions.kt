package ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.theme.PADDING_MED
import ui.theme.topAppBarContent

@Composable
fun BackAction(
    onBackClicked: () -> Unit
) {
    IconButton(onClick = onBackClicked) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Назад",
            tint = MaterialTheme.colors.topAppBarContent
        )
    }
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
) {
    IconButton(onClick = onSearchClicked) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Поиск",
            tint = MaterialTheme.colors.topAppBarContent
        )
    }
}

@Composable
fun CancelAction(
    onCancelClicked: () -> Unit
) {
    IconButton(onClick = onCancelClicked) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Отменить",
            tint = MaterialTheme.colors.topAppBarContent
        )
    }
}

@Composable
fun AddAction(
    onAddClicked: () -> Unit
) {
    IconButton(
        onClick = onAddClicked
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Добавить",
            tint = MaterialTheme.colors.topAppBarContent
        )
    }
}

@Composable
fun EditAction(
    onEditClick: () -> Unit
) {
    IconButton(
        onClick = onEditClick
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Редактировать",
            tint = MaterialTheme.colors.topAppBarContent
        )
    }
}

@Composable
fun MoreVertAction(
    expanded: Boolean,
    onActionClick: () -> Unit,
    onListClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Box{
    IconButton(
        onClick = { onActionClick() }
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "MoreVert",
            tint = MaterialTheme.colors.topAppBarContent
        )
    }
        DropdownMenu(expanded = expanded, onDismissRequest = { onActionClick() }) {
            Text(text = "Сохранить шаблон", modifier = Modifier
                .padding(PADDING_MED)
                .clickable { onSaveClick() })
            Divider()
            Text(text = "Список шаблонов",modifier = Modifier
                .padding(PADDING_MED)
                .clickable { onListClick() } )
        }
    }
}

@Preview
@Composable
fun PreviewIcons() {
    Column {
        BackAction {}
        SearchAction {}
        CancelAction {}
        AddAction {}
        EditAction {}
        MoreVertAction(expanded = false, onActionClick = {}, onListClick = {}, onSaveClick = {})
    }
}