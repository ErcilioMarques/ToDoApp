package com.example.to_docompose.ui.screens.list

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_docompose.R
import com.example.to_docompose.ui.theme.fabBackground

@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
) {
    Scaffold(
        topBar = {
            ListAppBar()
        },
        content = {},
        floatingActionButton = {
            ListFab(onFabCLicked = navigateToTaskScreen)
        },
    )
}

@Composable
fun ListFab(
    onFabCLicked: (taskId: Int) -> Unit,
) {
    FloatingActionButton(onClick = {
        onFabCLicked(-1)
    },
    backgroundColor = MaterialTheme.colors.fabBackground
        ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button),
            tint = Color.White,
        )
    }
}

@Composable
@Preview
private fun ListScreenPreview() {
    ListScreen(navigateToTaskScreen = {})
}
