package com.example.to_docompose.domain.models

import com.example.to_docompose.redux.State
import com.example.to_docompose.util.ActionLabels
import com.example.to_docompose.util.RequestState
import com.example.to_docompose.util.SearchAppBarState

data class ShowSnackBar(
    val opened: Boolean = false,
    val message: String = "",
    val label: ActionLabels = ActionLabels.NO_ACTION
)

data class TaskViewState(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.LOW,
    val searchAppBarState: SearchAppBarState = SearchAppBarState.CLOSED,
    val searchTextState: String = "",
    val selectedTask: ToDoTask? = null,
    val sortState: RequestState<Priority> =
        RequestState.Idle,
    val allTasks: RequestState<List<ToDoTask>> =
        RequestState.Idle,
    val searchedTasks: RequestState<List<ToDoTask>> =
        RequestState.Idle,
    val lowPriorityTasks: RequestState<List<ToDoTask>> =
        RequestState.Idle,
    val highPriorityTasks: RequestState<List<ToDoTask>> =
        RequestState.Idle,
    val showSnackBar: ShowSnackBar = ShowSnackBar()
) : State