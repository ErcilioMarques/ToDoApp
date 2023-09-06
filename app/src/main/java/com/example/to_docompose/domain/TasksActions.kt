package com.example.to_docompose.domain

import com.example.to_docompose.domain.models.Priority
import com.example.to_docompose.domain.models.ShowSnackBar
import com.example.to_docompose.domain.models.ToDoTask
import com.example.to_docompose.redux.Action
import com.example.to_docompose.util.RequestState
import com.example.to_docompose.util.SearchAppBarState

sealed class TasksActions : Action {
    object GetAllTasks : TasksActions()
    object GetSortState : TasksActions()
    object AddTask : TasksActions()
    object UpdateTask : TasksActions()
    object DeleteTask : TasksActions()
    object DeleteAllTasks : TasksActions()
    object NoActions : TasksActions()

    object UndoDeleteTask : TasksActions()

    data class SearchDatabase(val searchQuery: String) : TasksActions()
    data class PersistSortState(val priority: Priority) : TasksActions()
    data class GetSelectedTask(val taskId: Int) : TasksActions()
    data class FetchSelectedTask(val selectedTask: ToDoTask?) : TasksActions()
    data class UpdateTaskFields(val selectedTask: ToDoTask?) : TasksActions()
    data class UpdateTitle(val newTitle: String) : TasksActions()
    data class UpdateDescription(val newDescription: String) : TasksActions()
    data class UpdatePriority(val newPriority: Priority) : TasksActions()
    data class UpdateAppBarState(val newSearchAppBarState: SearchAppBarState) : TasksActions()
    data class UpdateSearchText(val newText: String) : TasksActions()
    data class ReadSortState(val newSortState: RequestState<Priority>) : TasksActions()
    data class UpdateSearchedTasks(val newSearchedTasks: RequestState<List<ToDoTask>>) :
        TasksActions()

    data class FetchAllTasks(val newAllTasks: RequestState<List<ToDoTask>>) :
        TasksActions()

    object GetAllLowPriorityTasks: TasksActions()

    object GetAllHighPriorityTasks: TasksActions()


    data class FetchAllLowPriorityTasks(
        val newAllTasks: RequestState<List<ToDoTask>>
    ) : TasksActions()

    data class FetchAllHighPriorityTasks(
        val newAllTasks: RequestState<List<ToDoTask>>
    ) : TasksActions()

  data class FetchShowSnackBar(
        val newShowSnackBar: ShowSnackBar
    ) : TasksActions()

    data class UpdateId(val newId: Int): TasksActions()


}
