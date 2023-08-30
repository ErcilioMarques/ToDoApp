package com.example.to_docompose.ui.screens.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.to_docompose.R
import com.example.to_docompose.ui.theme.fabBackground
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.Action
import com.example.to_docompose.util.SearchAppBarState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(key1 = true) {
        Log.d("ListScreen", "LaunchedEffect Trigged")
        sharedViewModel.getAllTasks()
    }

    val action by sharedViewModel.action

    val allTasks = sharedViewModel.allTasks.collectAsState()
    Log.d("ListScreen", "LaunchedEffect Trigged")

    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState


    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        handleDatabaseActions = { sharedViewModel.handleDatabaseActions(action = action) },
        onUndoClicked = {
            sharedViewModel.action.value = it
        },
        taskTitle = sharedViewModel.title.value,
        action = action
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        content = {
            ListContent(tasks = allTasks.value, navigateToTaskScreen)
        },
        floatingActionButton = {
            ListFab(onFabCLicked = navigateToTaskScreen)
        },
    )
}

@Composable
fun ListFab(
    onFabCLicked: (taskId: Int) -> Unit,
) {
    FloatingActionButton(
        onClick = {
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
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    handleDatabaseActions: () -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {
    handleDatabaseActions()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = action) {
        Log.d("ListScreen", "Snackbar")
        Log.d("ListScreen", action.toString())
        if (action != Action.NO_ACTION) {
            scope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = "${action.name}: $taskTitle",
                    actionLabel = setActionLabel(action),
                    duration = SnackbarDuration.Short
                )
                undoDeletedTask(action, result, onUndoClicked)
            }
        }
    }
}

private fun setActionLabel(action: Action): String {
    return if (action.name == "DELETE") {
        "UNDO"
    } else {
        "OK"
    }
}

private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
        onUndoClicked(Action.UNDO)
    }
}