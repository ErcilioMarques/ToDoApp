package com.example.to_docompose.domain

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.to_docompose.domain.models.Priority
import com.example.to_docompose.domain.models.ShowSnackBar
import com.example.to_docompose.domain.models.TaskViewState
import com.example.to_docompose.domain.models.ToDoTask
import com.example.to_docompose.domain.repositories.DataStoreRepository
import com.example.to_docompose.domain.repositories.ToDoRepository
import com.example.to_docompose.redux.Middleware
import com.example.to_docompose.redux.Store
import com.example.to_docompose.util.ActionLabels
import com.example.to_docompose.util.RequestState
import com.example.to_docompose.util.SearchAppBarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Task middleware.
 *
 * @property repository
 * @property dataStoreRepository
 * @constructor Create [TaskMiddleware]
 */
class TaskMiddleware(
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository
) : Middleware<TaskViewState, TasksActions> {
    override suspend fun process(
        action: TasksActions,
        currentState: TaskViewState,
        store: Store<TaskViewState, TasksActions>
    ) {
        Log.d("TaskMiddleware ->", action.toString())

        return when (action) {

            is TasksActions.GetAllTasks -> {
                getAllTasks(store = store)
            }

            is TasksActions.GetAllLowPriorityTasks -> {
                getLowPriorityTasks(store, scope = action.scope, started = action.started)
            }

            is TasksActions.GetAllHighPriorityTasks -> {
                getHighPriorityTasks(store, scope = action.scope, started = action.started)

            }

            is TasksActions.GetSortState -> {
                getSortState(store = store)
            }

            is TasksActions.AddTask -> {
                addTask(store = store, currentState = currentState)
            }

            is TasksActions.UpdateTask -> {
                updateTask(store = store, currentState = currentState)
            }

            is TasksActions.DeleteTask -> {
                deleteTask(store = store, currentState)
            }

            is TasksActions.DeleteAllTasks -> {
                deleteAllTasks(store = store)
            }

            is TasksActions.SearchDatabase -> {
                searchDatabase(store, searchQuery = action.searchQuery)
            }

            is TasksActions.PersistSortState -> {
                persistSortState(priority = action.priority)
            }

            is TasksActions.GetSelectedTask -> {
                getSelectedTask(store = store, taskId = action.taskId)
            }

            else -> {}
        }
    }


    /**
     * Add task.
     *
     * @param store Store
     * @param currentState Current state
     */
    private suspend fun addTask(
        store: Store<TaskViewState, TasksActions>,
        currentState: TaskViewState
    ) {
        val toDoTask = ToDoTask(
            title = currentState.title,
            description = currentState.description,
            priority = currentState.priority
        )
        repository.addTask(toDoTask = toDoTask)
        store.dispatch(TasksActions.UpdateAppBarState(SearchAppBarState.CLOSED))
        store.dispatch(
            TasksActions.FetchShowSnackBar(
                ShowSnackBar(
                    opened = true,
                    label = ActionLabels.ADD,
                    message = "ADD: ${toDoTask.title}"
                )
            )
        )
    }

    /**
     * Update task.
     *
     * @param currentState Current state
     */
    private suspend fun updateTask(
        store: Store<TaskViewState, TasksActions>,
        currentState: TaskViewState
    ) {
        val toDoTask = ToDoTask(
            id = currentState.id,
            title = currentState.title,
            description = currentState.description,
            priority = currentState.priority
        )
        repository.updateTask(toDoTask = toDoTask)
        store.dispatch(
            TasksActions.FetchShowSnackBar(
                ShowSnackBar(
                    opened = true,
                    label = ActionLabels.UPDATE,
                    message = "UPDATE: ${toDoTask.title}"
                )
            )
        )

    }

    /**
     * Delete task.
     *
     * @param currentState Current state
     */
    private suspend fun deleteTask(
        store: Store<TaskViewState, TasksActions>,
        currentState: TaskViewState
    ) {

        val toDoTask = ToDoTask(
            id = currentState.id,
            title = currentState.title,
            description = currentState.description,
            priority = currentState.priority
        )
        repository.deleteTask(toDoTask = toDoTask)
        store.dispatch(
            TasksActions.FetchShowSnackBar(
                ShowSnackBar(
                    opened = true,
                    label = ActionLabels.DELETE,
                    message = "DELETE: ${toDoTask.title}"
                )
            )
        )
    }

    /**
     * Delete all tasks.
     */
    private suspend fun deleteAllTasks(
        store: Store<TaskViewState, TasksActions>,
    ) {
        repository.deleteAllTasks()
        store.dispatch(
            TasksActions.FetchShowSnackBar(
                ShowSnackBar(
                    opened = true,
                    label = ActionLabels.DELETE,
                    message = "All Tasks Removed"
                )
            )
        )
    }

    /**
     * Get all tasks.
     *
     * @param store Store
     */
    private suspend fun getAllTasks(store: Store<TaskViewState, TasksActions>) {
        store.dispatch(
            TasksActions.FetchAllTasks(

                RequestState.Loading

            )
        )

        try {
            repository.getAllTasks.collect {
                store.dispatch(
                    TasksActions.FetchAllTasks(

                        RequestState.Success(it)

                    )
                )
            }
        } catch (e: Exception) {
            store.dispatch(
                TasksActions.FetchAllTasks(

                    RequestState.Error(e)

                )
            )
        }
    }

    /**
     * Get low priority tasks.
     *
     * @param store Store
     * @param scope Scope
     * @param started Started
     */
    private suspend fun getLowPriorityTasks(
        store: Store<TaskViewState, TasksActions>,
        scope: CoroutineScope,
        started: SharingStarted
    ) {
        store.dispatch(
            TasksActions.FetchAllLowPriorityTasks(
                repository.sortByLowPriority.stateIn(
                    scope = scope,
                    started = SharingStarted.WhileSubscribed(),
                    initialValue = emptyList()
                ).value
            )
        )
    }

    /**
     * Get high priority tasks.
     *
     * @param store Store
     * @param scope Scope
     * @param started Started
     */
    private suspend fun getHighPriorityTasks(
        store: Store<TaskViewState, TasksActions>,
        scope: CoroutineScope,
        started: SharingStarted
    ) {
        store.dispatch(
            TasksActions.FetchAllLowPriorityTasks(
                repository.sortByHighPriority.stateIn(
                    scope = scope,
                    started = SharingStarted.WhileSubscribed(),
                    initialValue = emptyList()
                ).value
            )
        )
    }

    /**
     * Search database.
     *
     * @param store Store
     * @param searchQuery Search query
     */
    private suspend fun searchDatabase(
        store: Store<TaskViewState, TasksActions>,
        searchQuery: String
    ) {
        store.dispatch(
            TasksActions.UpdateSearchedTasks(

                RequestState.Loading

            )
        )
        try {
            repository.searchDatabase(searchQuery = "%$searchQuery%")
                .collect { searchedTasks ->
                    store.dispatch(
                        TasksActions.UpdateSearchedTasks(

                            RequestState.Success(
                                searchedTasks
                            )

                        )
                    )
                }

        } catch (e: Exception) {
            store.dispatch(
                TasksActions.UpdateSearchedTasks(

                    RequestState.Error(e)
                )

            )
        }
        store.dispatch(TasksActions.UpdateAppBarState(SearchAppBarState.TRIGGERED))
    }

    /**
     * Get sort state.
     *
     * @param store Store
     */
    private suspend fun getSortState(store: Store<TaskViewState, TasksActions>) {
        store.dispatch(
            TasksActions.ReadSortState(

                RequestState.Loading

            )
        )
        try {
            dataStoreRepository.readSortState
                .map { Priority.valueOf(it) }
                .collect {

                    store.dispatch(
                        TasksActions.ReadSortState(
                            RequestState.Success(it)
                        )
                    )
                }
        } catch (e: Exception) {
            store.dispatch(
                TasksActions.ReadSortState(
                    RequestState.Error(e)
                )
            )
        }
    }


    /**
     * Get selected task.
     *
     * @param store Store
     * @param taskId Task id
     */
    private suspend fun getSelectedTask(store: Store<TaskViewState, TasksActions>, taskId: Int) {
        repository.getSelectedTask(taskId = taskId).collect { task ->
            store.dispatch(
                TasksActions.FetchSelectedTask(
                    selectedTask = task
                )
            )
            if (task != null) {
                store.dispatch(
                    TasksActions.UpdateDescription(
                        newDescription = task.description
                    )
                )
                store.dispatch(
                    TasksActions.UpdateTitle(
                        newTitle = task.title
                    )
                )
                store.dispatch(
                    TasksActions.UpdatePriority(
                        newPriority = task.priority

                    )
                )

                store.dispatch(
                    TasksActions.UpdateId(
                        newId = taskId
                    )
                )
            }

        }
    }


    /**
     * Persist sort state.
     *
     * @param priority Priority
     */
    private suspend fun persistSortState(priority: Priority) {
        dataStoreRepository.persistSortState(priority = priority)
    }
}