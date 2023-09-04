package com.example.to_docompose.domain.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.to_docompose.redux.State
import com.example.to_docompose.util.ActionLabels
import com.example.to_docompose.util.RequestState
import com.example.to_docompose.util.SearchAppBarState

data class ShowSnackBar (val opened: Boolean = false, val message: String = "", val label: ActionLabels = ActionLabels.NO_ACTION)

data class TaskViewState(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.LOW,
    val searchAppBarState: SearchAppBarState = SearchAppBarState.CLOSED,
    val searchTextState: String = "",
    val selectedTask: ToDoTask? = null,
    val sortState: MutableState<RequestState<Priority>> = mutableStateOf(
        RequestState.Idle
    ),
    val allTasks: MutableState<RequestState<List<ToDoTask>>> = mutableStateOf(
        RequestState.Idle
    ),
    val searchedTasks: MutableState<RequestState<List<ToDoTask>>> = mutableStateOf(
        RequestState.Idle
    ),
    val lowPriorityTasks: List<ToDoTask> = emptyList(),
    val highPriorityTasks: List<ToDoTask> = emptyList(),
    val showSnackBar: ShowSnackBar = ShowSnackBar()
) : State