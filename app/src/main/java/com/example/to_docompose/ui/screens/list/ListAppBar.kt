package com.example.to_docompose.ui.screens.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_docompose.R
import com.example.to_docompose.components.DisplayAlertDialog
import com.example.to_docompose.components.PriorityItem
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.ui.theme.LARGE_PADDING
import com.example.to_docompose.ui.theme.TOP_APP_BAR_HEIGHT
import com.example.to_docompose.ui.theme.Typography
import com.example.to_docompose.ui.theme.topAppBarBackgroundColor
import com.example.to_docompose.ui.theme.topAppBarContentColor
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.Action
import com.example.to_docompose.util.SearchAppBarState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel, searchAppBarState: SearchAppBarState, searchTextState: String
) {

    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppbar(onSearchClicked = {
                sharedViewModel.updateAppBarState(SearchAppBarState.OPENED)
            }, onSortClicked = {
                sharedViewModel.persistSortState(it)
            }, onDeleteAllConfirmed = {
                sharedViewModel.updateAction(newAction = Action.DELETE_ALL)
            })
        }

        else -> SearchAppBar(text = searchTextState,
            onTextChange = { newText -> sharedViewModel.updateSearchText(newText) },
            onCloseClicked = {
                sharedViewModel.updateAppBarState(SearchAppBarState.CLOSED)
                sharedViewModel.updateSearchText("")
            },
            onSearchClicked = {
                sharedViewModel.searchDatabase(searchQuery = it)
            })

    }

}

@Composable
fun DefaultListAppbar(
    onSearchClicked: () -> Unit, onSortClicked: (Priority) -> Unit, onDeleteAllConfirmed: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.list_screen_title),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onSortClicked = onSortClicked,
                onDeleteAllConfirmed = onDeleteAllConfirmed
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit, onSortClicked: (Priority) -> Unit, onDeleteAllConfirmed: () -> Unit
) {
    var openDialog by remember {
        mutableStateOf(false)
    }
    DisplayAlertDialog(title = stringResource(id = R.string.delete_all_task),
        message = stringResource(
            id = R.string.delete_all_task_confirmation
        ),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = {
            onDeleteAllConfirmed()
        })
    SearchAction(onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(onDeleteAllConfirmed = { openDialog = true })
}

@Composable
fun SearchAction(onSearchClicked: () -> Unit) {
    IconButton(onClick = { onSearchClicked() }) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.search_text),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun SortAction(onSortClicked: (Priority) -> Unit) {

    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list_24),
            contentDescription = stringResource(R.string.sort_actions),
            tint = MaterialTheme.colors.topAppBarContentColor

        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Priority.values().slice(setOf(0, 2, 3)).forEach { priority ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    onSortClicked(priority)
                }) {
                    PriorityItem(priority = priority)
                }
            }
        }
    }

}

@Composable
fun DeleteAllAction(onDeleteAllConfirmed: () -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_vertical_more_vert_24),
            contentDescription = stringResource(R.string.delete_all),
            tint = MaterialTheme.colors.topAppBarContentColor

        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = {
                expanded = false
                onDeleteAllConfirmed()
            }) {
                Text(
                    modifier = Modifier.padding(start = LARGE_PADDING),
                    text = stringResource(R.string.delete_all),
                    style = Typography.subtitle2
                )
            }
        }
    }
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topAppBarBackgroundColor
    ) {
        TextField(modifier = Modifier.fillMaxWidth().testTag("searchTextFieldTag"), value = text, onValueChange = {
            onTextChange(it)
        }, placeholder = {
            Text(
                modifier = Modifier.alpha(ContentAlpha.medium),
                text = stringResource(R.string.search_placeholder),
                color = Color.White
            )
        }, textStyle = TextStyle(
            color = MaterialTheme.colors.topAppBarContentColor,
            fontSize = MaterialTheme.typography.subtitle1.fontSize
        ), singleLine = true, leadingIcon = {
            IconButton(
                modifier = Modifier.alpha(ContentAlpha.disabled),
                onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search_icon),
                    tint = MaterialTheme.colors.topAppBarContentColor
                )
            }
        }, trailingIcon = {
            IconButton(onClick = {
                if (text.isNotEmpty()) {
                    onTextChange("")
                } else {
                    onCloseClicked()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.close_icon),
                    tint = MaterialTheme.colors.topAppBarContentColor
                )
            }
        },

            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ), keyboardActions = KeyboardActions(onSearch = {
                onSearchClicked(text)
            }), colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.topAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent

            )
        )
    }
}

@Composable
@Preview
private fun DefaultListAppBarPreview() {
    DefaultListAppbar(onSearchClicked = {}, onSortClicked = {}, onDeleteAllConfirmed = {})
}

@Composable
@Preview
fun SearchAppBarPreview() {
    SearchAppBar(text = "", onTextChange = {}, onCloseClicked = { }, onSearchClicked = {})
}