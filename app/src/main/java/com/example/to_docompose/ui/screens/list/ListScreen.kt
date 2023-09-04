package com.example.to_docompose.ui.screens.list

import android.annotation.SuppressLint
import android.util.Log
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
import com.example.to_docompose.domain.models.TaskViewState
import com.example.to_docompose.ui.theme.fabBackground
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.ActionLabels
import com.example.to_docompose.util.SearchAppBarState
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {

    val viewState by sharedViewModel.viewState.collectAsState()


    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        onComplete = { sharedViewModel.dispatchActions(it) },
        onUndoClicked = {
            sharedViewModel.dispatchActions(it)
        },
        taskTitle = sharedViewModel.viewState.value.title,
        viewState = viewState,
        sharedViewModel = sharedViewModel
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = viewState.searchAppBarState,
                searchTextState = viewState.searchTextState
            )
        },
        content = {
            ListContent(
                allTasks = viewState.allTasks.value,
                lowPriorityTasks = viewState.lowPriorityTasks,
                highPriorityTasks = viewState.highPriorityTasks,
                sortState = viewState.sortState.value,
                searchedTasks = viewState.searchedTasks.value,
                searchAppBarState = viewState.searchAppBarState,
                onSwipeToDelete = { action, task ->
                    sharedViewModel.dispatchActions(action)
                    sharedViewModel.updateTaskFields(selectedTask = task)
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                },
                navigateToTaskScreen = navigateToTaskScreen,
                sharedViewModel = sharedViewModel
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
    viewState: TaskViewState,
    sharedViewModel: SharedViewModel
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = viewState.showSnackBar.label) {
        if (viewState.showSnackBar.label != ActionLabels.NO_ACTION) {
            scope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(viewState.showSnackBar.label, taskTitle),
                    actionLabel = setActionLabel(viewState.showSnackBar.label),
                    duration = SnackbarDuration.Short
                )
                undoDeletedTask(viewState.showSnackBar.label, result, onUndoClicked)
            }
            onComplete(TasksActions.NoActions)
        }
    }
}

private fun setMessage(action: ActionLabels, taskTitle: String): String {
    return when (action) {
        ActionLabels.DELETE_ALL -> "All Tasks Removed"
        else -> "${action}: $taskTitle"
    }
}

private fun setActionLabel(action: ActionLabels): String {
    return if (action == ActionLabels.DELETE) {
        "UNDO"
    } else {
        "OK"
    }
}

private fun undoDeletedTask(
    action: ActionLabels,
    snackBarResult: SnackbarResult,
    onUndoClicked: (TasksActions) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed && action == ActionLabels.DELETE) {
        onUndoClicked(TasksActions.UndoDeleteTask)
    }
}