package com.example.to_docompose.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_docompose.domain.TasksActions
import com.example.to_docompose.domain.TasksStore
import com.example.to_docompose.domain.models.Priority
import com.example.to_docompose.domain.models.TaskViewState
import com.example.to_docompose.domain.models.ToDoTask
import com.example.to_docompose.domain.repositories.DataStoreRepository
import com.example.to_docompose.domain.repositories.ToDoRepository
import com.example.to_docompose.redux.Store
import com.example.to_docompose.util.Action
import com.example.to_docompose.util.Constants.MAX_TITLE_LENGTH
import com.example.to_docompose.util.RequestState
import com.example.to_docompose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val store: TasksStore,
) : ViewModel() {
    val viewState: StateFlow<TaskViewState> = store.state

    var action by mutableStateOf(Action.NO_ACTION)
        private set

    //
//    var id by mutableStateOf(0)
//        private set
//    var title by mutableStateOf("")
//        private set
//    var description by mutableStateOf("")
//        private set
//    var priority by mutableStateOf(Priority.LOW)
//        private set
//
//    var searchAppBarState by mutableStateOf(SearchAppBarState.CLOSED)
//        private set
//    var searchTextState by mutableStateOf("")
//        private set
//
//    private val _allTasks =
//        MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
//    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks
//
//    private val _searchedTasks =
//        MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
//    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = _searchedTasks
//
//    private val _sortState =
//        MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
//    val sortState: StateFlow<RequestState<Priority>> = _sortState
//
//    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
//    val selectedTask: StateFlow<ToDoTask?> = _selectedTask
    init {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(TasksActions.GetAllTasks)
            store.dispatch(TasksActions.GetSortState)

        }

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

    fun getLowPriorityTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                store.dispatch(
                    TasksActions.GetAllLowPriorityTasks(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed()
                    )
                )
            }
        }
    }

    fun getHighPriorityTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                store.dispatch(
                    TasksActions.GetAllHighPriorityTasks(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed()
                    )
                )
            }
        }
    }

    fun readSortState() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                store.dispatch(
                    TasksActions.GetSortState
                )
            }
        }
    }

    fun persistSortState(priority: Priority) {
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

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(TasksActions.AddTask)
        }
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(TasksActions.UpdateTask)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(TasksActions.DeleteTask)
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(TasksActions.DeleteTask)

        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> {
                addTask()
            }

            Action.UPDATE -> {
                updateTask()
            }

            Action.DELETE -> {
                deleteTask()
            }

            Action.DELETE_ALL -> {
                deleteAllTasks()
            }

            Action.UNDO -> {
                addTask()
            }

            else -> {

            }
        }
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

//    fun updateAction(newAction: Action) {
//        action = newAction
//    }

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