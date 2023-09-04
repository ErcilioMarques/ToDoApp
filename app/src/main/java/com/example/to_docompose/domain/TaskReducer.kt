package com.example.to_docompose.domain

import com.example.to_docompose.domain.models.Priority
import com.example.to_docompose.domain.models.TaskViewState
import com.example.to_docompose.redux.Reducer

/**
 * Task reducer.
 *
 * @constructor Create empty constructor for task reducer
 */
class TaskReducer : Reducer<TaskViewState, TasksActions> {
    override fun reduce(currentState: TaskViewState, action: TasksActions): TaskViewState {
        return when (action) {
            is TasksActions.FetchAllTasks -> {
                currentState.copy(allTasks = action.newAllTasks)
            }

            is TasksActions.FetchAllHighPriorityTasks -> {
                currentState.copy(highPriorityTasks = action.newAllTasks)
            }

            is TasksActions.FetchAllLowPriorityTasks -> {
                currentState.copy(lowPriorityTasks = action.newAllTasks)
            }

            is TasksActions.UpdateTaskFields -> {
                if (action.selectedTask != null) {
                    currentState.copy(
                        id = action.selectedTask.id,
                        title = action.selectedTask.title,
                        description = action.selectedTask.description,
                        priority = action.selectedTask.priority,
                    )

                } else {
                    currentState.copy(
                        id = 0,
                        title = "",
                        description = "",
                        priority = Priority.LOW,
                    )
                }
            }

            is TasksActions.UpdateTitle -> {
                currentState.copy(title = action.newTitle)

            }

            is TasksActions.UpdateDescription -> {
                currentState.copy(description = action.newDescription)

            }

            is TasksActions.UpdatePriority -> {
                currentState.copy(priority = action.newPriority)
            }

            is TasksActions.UpdateAppBarState -> {
                currentState.copy(searchAppBarState = action.newSearchAppBarState)
            }

            is TasksActions.UpdateSearchedTasks -> {
                currentState.copy(searchedTasks = action.newSearchedTasks)
            }

            is TasksActions.ReadSortState -> {
                currentState.copy(sortState = action.newSortState)
            }

            is TasksActions.UpdateSearchText -> {
                currentState
            }

            else -> currentState
        }
    }
}