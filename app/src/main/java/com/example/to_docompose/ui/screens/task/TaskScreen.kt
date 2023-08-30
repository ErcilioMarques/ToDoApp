package com.example.to_docompose.ui.screens.task

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.example.to_docompose.data.models.ToDoTask
import com.example.to_docompose.util.Action

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    navigateToListScreen: (Action) -> Unit
) {
    Scaffold(
        topBar = {
            TaskAppBar(selectedTask = selectedTask, navigateToListScreen = navigateToListScreen)
        },
        content = {
        }
    )
}