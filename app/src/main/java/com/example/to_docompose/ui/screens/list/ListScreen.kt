package com.example.to_docompose.ui.screens.list

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
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
import com.example.to_docompose.domain.TasksActions
import com.example.to_docompose.domain.TasksStore
import com.example.to_docompose.ui.theme.fabBackground
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.SearchAppBarState
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun ListScreen(
    store: TasksStore,
    action: TasksActions,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {

    LaunchedEffect(key1 = action) {
//        sharedViewModel.handleDatabaseActions(action = action)
        store.dispatch(action)

    }

    val allTasks by sharedViewModel.viewState.value.allTasks.collectAsState()
    val searchedTasks by sharedViewModel.viewState.value.searchedTasks.collectAsState()

    val sortState by sharedViewModel.viewState.value.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.viewState.value.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.viewState.value.highPriorityTasks.collectAsState()

    val searchAppBarState: SearchAppBarState = sharedViewModel.viewState.value.searchAppBarState
    val searchTextState: String = sharedViewModel.viewState.value.searchTextState


    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        onComplete = { sharedViewModel.dispatchActions(it) },
        onUndoClicked = {
            sharedViewModel.dispatchActions(it)
        },
        taskTitle = sharedViewModel.viewState.value.title,
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
            ListContent(
                allTasks = allTasks,
                lowPriorityTasks = lowPriorityTasks,
                highPriorityTasks = highPriorityTasks,
                sortState = sortState,
                searchedTasks = searchedTasks,
                searchAppBarState = searchAppBarState,
                onSwipeToDelete = { action, task ->
                    sharedViewModel.dispatchActions(action)
                    sharedViewModel.updateTaskFields(selectedTask = task)
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                },
                navigateToTaskScreen = navigateToTaskScreen
            )
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
    onComplete: (TasksActions) -> Unit,
    onUndoClicked: (TasksActions) -> Unit,
    taskTitle: String,
    action: TasksActions
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = action) {
        if (action != TasksActions.NoActions) {
            scope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(action, taskTitle),
                    actionLabel = setActionLabel(action),
                    duration = SnackbarDuration.Short
                )
                undoDeletedTask(action, result, onUndoClicked)
            }
            onComplete(TasksActions.NoActions)
        }
    }
}

private fun setMessage(action: TasksActions, taskTitle: String): String {
    return when (action) {
        TasksActions.DeleteAllTasks -> "All Tasks Removed"
        else -> "${action}: $taskTitle"
    }
}

private fun setActionLabel(action: TasksActions): String {
    return if (action == TasksActions.DeleteTask) {
        "UNDO"
    } else {
        "OK"
    }
}

private fun undoDeletedTask(
    action: TasksActions,
    snackBarResult: SnackbarResult,
    onUndoClicked: (TasksActions) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed && action == TasksActions.DeleteTask) {
        onUndoClicked(TasksActions.UndoDeleteTask)
    }
}