package com.example.to_docompose.ui.screens.task

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_docompose.R
import com.example.to_docompose.domain.TasksActions
import com.example.to_docompose.ui.components.DisplayAlertDialog
import com.example.to_docompose.domain.models.Priority
import com.example.to_docompose.domain.models.ToDoTask
import com.example.to_docompose.ui.theme.topAppBarBackgroundColor
import com.example.to_docompose.ui.theme.topAppBarContentColor
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.ActionLabels

@Composable
fun TaskAppBar(
    sharedViewModel: SharedViewModel,
    selectedTask: ToDoTask?, navigateToListScreen: (ActionLabels) -> Unit
) {
    if (selectedTask == null) {
        NewTaskAppBar(
            sharedViewModel = sharedViewModel,
            navigateToListScreen = navigateToListScreen
        )
    } else {
        ExistingTaskAppBar(
            sharedViewModel = sharedViewModel,
            navigateToListScreen = navigateToListScreen,
            selectedTask = selectedTask
        )
    }
}

@Composable
fun NewTaskAppBar(
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (ActionLabels) -> Unit
) {
    TopAppBar(navigationIcon = {
        BackAction(onBackClicked = navigateToListScreen)
    }, title = {
        Text(
            text = stringResource(R.string.add_task),
            color = MaterialTheme.colors.topAppBarContentColor
        )
    }, backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor, actions = {
        AddAction(onAddClicked = {
            sharedViewModel.addTask()
            navigateToListScreen(ActionLabels.ADD)
        })
    })
}

@Composable
fun ExistingTaskAppBar(
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (ActionLabels) -> Unit, selectedTask: ToDoTask
) {
    TopAppBar(navigationIcon = {
        CloseAction(onCloseClicked = navigateToListScreen)
    }, title = {
        Text(
            text = selectedTask.title,
            color = MaterialTheme.colors.topAppBarContentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }, backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor, actions = {
        ExistingTaskAppBarActions(
            sharedViewModel = sharedViewModel,
            selectedTask = selectedTask,
            navigateToListScreen = navigateToListScreen
        )
    })
}

@Composable
fun ExistingTaskAppBarActions(
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (ActionLabels) -> Unit, selectedTask: ToDoTask
) {
    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(title = stringResource(id = R.string.delete_task, selectedTask.title),
        message = stringResource(
            id = R.string.delete_task_confirmation, selectedTask.title
        ),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = {
            sharedViewModel.deleteTask()
            navigateToListScreen(ActionLabels.DELETE)
        }
    )

    DeleteAction(onDeleteClicked = {
        openDialog = true
    })
    UpdateAction(onUpdateClicked = navigateToListScreen)
}

@Composable
fun BackAction(
    onBackClicked: (ActionLabels) -> Unit
) {
    IconButton(onClick = { onBackClicked(ActionLabels.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_arrow),
            tint = MaterialTheme.colors.topAppBarContentColor
        )

    }
}

@Composable
fun AddAction(
    onAddClicked: (ActionLabels) -> Unit
) {
    IconButton(onClick = {
        onAddClicked(ActionLabels.ADD)
    }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.add_task_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )

    }
}

@Composable
fun CloseAction(
    onCloseClicked: (ActionLabels) -> Unit
) {
    IconButton(onClick = { onCloseClicked(ActionLabels.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.close_icon_task),
            tint = MaterialTheme.colors.topAppBarContentColor
        )

    }
}

@Composable
fun DeleteAction(
    onDeleteClicked: () -> Unit
) {
    IconButton(onClick = { onDeleteClicked() }) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.delete_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )

    }
}

@Composable
fun UpdateAction(
    onUpdateClicked: (ActionLabels) -> Unit
) {
    IconButton(onClick = { onUpdateClicked(ActionLabels.UPDATE) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.update_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )

    }
}

//@Composable
//@Preview
//fun NewTaskAppBarPreview() {
//    NewTaskAppBar(navigateToListScreen = {})
//}

//@Composable
//@Preview
//fun ExistingTaskAppBarPreview() {
//    ExistingTaskAppBar(
//        navigateToListScreen = {}, selectedTask = ToDoTask(
//            id = 0,
//            title = "Mars Miki",
//            description = "SOme random desc",
//            priority = Priority.MEDIUM
//        )
//    )
//}