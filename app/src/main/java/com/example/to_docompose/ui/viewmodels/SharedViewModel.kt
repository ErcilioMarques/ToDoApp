package com.example.to_docompose.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_docompose.domain.TasksActions
import com.example.to_docompose.domain.TasksStore
import com.example.to_docompose.domain.models.Priority
import com.example.to_docompose.domain.models.ShowSnackBar
import com.example.to_docompose.domain.models.TaskViewState
import com.example.to_docompose.domain.models.ToDoTask
import com.example.to_docompose.util.ActionLabels
import com.example.to_docompose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val store: TasksStore,
) : ViewModel() {
    val viewState: StateFlow<TaskViewState> = store.state

    var action by mutableStateOf(ActionLabels.NO_ACTION)
        private set

    init {
        getAllTasks()
        readSortState()
    }

    fun dispatchActions(action: TasksActions) {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(
                action
            )
        }
    }

    fun searchDatabase(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(
                TasksActions.SearchDatabase(searchQuery)
            )
        }
    }

    private fun getLowPriorityTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                store.dispatch(
                    TasksActions.GetAllLowPriorityTasks
                )
            }
        }
    }

    private fun getHighPriorityTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                store.dispatch(
                    TasksActions.GetAllHighPriorityTasks
                )
            }
        }
    }

    private fun readSortState() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                store.dispatch(
                    TasksActions.GetSortState
                )
            }
        }
    }

    fun persistSortState(priority: Priority) {

        when (priority) {
            Priority.LOW -> getLowPriorityTasks()
            Priority.HIGH -> getHighPriorityTasks()
            else -> getAllTasks()
        }

        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(
                TasksActions.PersistSortState(priority = priority)
            )
        }
    }

    private fun getAllTasks() {

        viewModelScope.launch {
            store.dispatch(
                TasksActions.GetAllTasks
            )
        }
    }

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            store.dispatch(
                TasksActions.GetSelectedTask(taskId = taskId)
            )
        }
    }

    fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(TasksActions.AddTask)
        }
    }

    fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(TasksActions.UpdateTask)
        }
    }

    fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(TasksActions.DeleteTask)
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(TasksActions.DeleteAllTasks)

        }
    }

    suspend fun updateShowSnackBar(newShowSnackBar: ShowSnackBar) {
        store.dispatch(TasksActions.FetchShowSnackBar(newShowSnackBar = newShowSnackBar))

    }

    fun updateTaskFields(selectedTask: ToDoTask?) {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(TasksActions.UpdateTaskFields(selectedTask = selectedTask))
        }
    }

    fun updateTitle(newTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(TasksActions.UpdateTitle(newTitle = newTitle))
        }
    }

    fun updateDescription(newDescription: String) {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(TasksActions.UpdateDescription(newDescription = newDescription))
        }
    }

    fun updatePriority(newPriority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(TasksActions.UpdatePriority(newPriority = newPriority))
        }
    }

    fun updateAppBarState(newState: SearchAppBarState) {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(TasksActions.UpdateAppBarState(newSearchAppBarState = newState))
        }
    }

    fun updateSearchText(newText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(TasksActions.UpdateSearchText(newText = newText))
        }
    }

    fun validateFields(): Boolean {
        return store.state.value.title.isNotEmpty() && store.state.value.description.isNotEmpty()
    }

}