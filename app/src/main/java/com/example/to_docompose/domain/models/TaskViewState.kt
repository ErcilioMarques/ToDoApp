package com.example.to_docompose.domain.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.to_docompose.redux.State
import com.example.to_docompose.util.Action
import com.example.to_docompose.util.RequestState
import com.example.to_docompose.util.SearchAppBarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class TaskViewState(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.LOW,
    val searchAppBarState: SearchAppBarState = SearchAppBarState.CLOSED,
    val searchTextState: String = "",
    val selectedTask: ToDoTask? = null,
    val sortState: StateFlow<RequestState<Priority>> = MutableStateFlow<RequestState<Priority>>(
        RequestState.Idle
    ),
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = MutableStateFlow<RequestState<List<ToDoTask>>>(
        RequestState.Idle
    ),
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = MutableStateFlow<RequestState<List<ToDoTask>>>(
        RequestState.Idle
    ),
    val lowPriorityTasks: StateFlow<List<ToDoTask>> = MutableStateFlow<List<ToDoTask>>(emptyList()),
    val highPriorityTasks: StateFlow<List<ToDoTask>> = MutableStateFlow<List<ToDoTask>>(emptyList()),
    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)
) : State