package com.example.to_docompose.ui.screens.task

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.to_docompose.domain.models.Priority
import com.example.to_docompose.domain.models.ToDoTask
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.Action

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun TaskScreen(
    sharedViewModel: SharedViewModel,
    selectedTask: ToDoTask?,
    navigateToListScreen: (Action) -> Unit
) {
    val title: String = sharedViewModel.viewState.value.title
    val description: String = sharedViewModel.viewState.value.description
    val priority: Priority = sharedViewModel.viewState.value.priority
    val context = LocalContext.current

    BackHandler {
        navigateToListScreen(Action.NO_ACTION)
    }

    Scaffold(
        topBar = {
            TaskAppBar(selectedTask = selectedTask, navigateToListScreen = { action ->

                if (action == Action.NO_ACTION) {
                    navigateToListScreen(action)

                } else {
                    if (sharedViewModel.validateFields()) {
                        navigateToListScreen(action)
                    } else {
                        displayToast(context = context)
                    }
                }
            })
        },
        content = {
            TaskContent(
                title = title,
                onTitleChange = {
                    sharedViewModel.updateTitle(it)
                },
                description = description,
                onDescriptionChange = {
                    sharedViewModel.updateDescription(it)
                },
                priority = priority,
                onPriorityChange = {
                    sharedViewModel.updatePriority(it)
                }
            )
        }
    )
}


fun displayToast(context: Context) {
    Toast.makeText(context, "Fields are empty", Toast.LENGTH_SHORT).show()
}