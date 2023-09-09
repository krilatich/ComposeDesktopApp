package ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.theme.topAppBarBackground
import ui.theme.topAppBarContent

private val HEIGHT_APP_BAR = 56.dp

/**
 * Наиболее простой AppBar с титулом и стрелкой налево.
 */
@Composable
fun CommonAppBar(
    title: String,
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val backComp: (@Composable () -> Unit)? = if (onBackClick != null) {
        { BackAction(onBackClicked = onBackClick) }
    } else null

    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = backComp,
        actions = actions,
        elevation = elevation,
        backgroundColor = MaterialTheme.colors.topAppBarBackground,
        contentColor = MaterialTheme.colors.topAppBarContent
    )
}

@Composable
fun SearchListAppBar(
    text: String,
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    focusRequester: FocusRequester = remember { FocusRequester() },
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(HEIGHT_APP_BAR),
        elevation = elevation,
        color = MaterialTheme.colors.topAppBarBackground
    ) {
        TextField(
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                // URL: https://stackoverflow.com/questions/64181930/request-focus-on-textfield-in-jetpack-compose
                .focusRequester(focusRequester),
            placeholder = {
                Text(
                    text = "Найти",
                    color = MaterialTheme.colors.topAppBarContent,
                    modifier = Modifier.alpha(ContentAlpha.medium)
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.topAppBarContent,
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Найти",
                    tint = MaterialTheme.colors.topAppBarContent,
                    modifier = Modifier.alpha(ContentAlpha.disabled)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isEmpty()) {
                            onCloseClicked()
                        } else {
                            onTextChange("")
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Закрыть",
                        tint = MaterialTheme.colors.topAppBarContent
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.topAppBarContent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )
        )
    }
}

@Preview()
@Composable
private fun AppBarPreview() {
    MaterialTheme {
        CommonAppBar(
            title = "Заголовок",
            onBackClick = {}
        )
    }
}

@Preview
@Composable
private fun AppBarSearchPreview() {
    MaterialTheme {
        SearchListAppBar(
            text = "",
            onTextChange = {},
            onCloseClicked = { },
            onSearchClicked = { }
        )
    }
}
