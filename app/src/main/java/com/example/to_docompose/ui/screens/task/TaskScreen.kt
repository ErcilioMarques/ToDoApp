package com.example.to_docompose.ui.screens.task

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.to_docompose.domain.models.Priority
import com.example.to_docompose.domain.models.ToDoTask
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.ActionLabels

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun TaskScreen(
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (ActionLabels) -> Unit
) {
    val context = LocalContext.current
    val viewState by sharedViewModel.viewState.collectAsState()

    LaunchedEffect(key1 = viewState){
        Log.d("viewState", "viewState1 -> ${viewState}")
    }

    BackHandler {
        navigateToListScreen(ActionLabels.NO_ACTION)
    }

    Scaffold(
        topBar = {
            TaskAppBar(sharedViewModel = sharedViewModel,selectedTask = viewState.selectedTask, navigateToListScreen = {
                    action ->

                if (action == ActionLabels.NO_ACTION) {
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
                onTitleChange = {
                    sharedViewModel.updateTitle(it)
                },
                onDescriptionChange = {
                    sharedViewModel.updateDescription(it)
                },
                onPriorityChange = {
                    sharedViewModel.updatePriority(it)
                },
                viewState = viewState
            )
        }
    )
}


fun displayToast(context: Context) {
    Toast.makeText(context, "Fields are empty", Toast.LENGTH_SHORT).show()
}