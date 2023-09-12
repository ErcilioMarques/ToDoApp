package com.example.to_docompose.domain

import com.example.to_docompose.data.repositories.interfaces.IDataStoreRepository
import com.example.to_docompose.data.repositories.interfaces.IToDoRepository
import com.example.to_docompose.domain.models.TaskViewState
import com.example.to_docompose.redux.BaseStore
import com.example.to_docompose.redux.Reducer
import javax.inject.Inject

/**
 * Tasks store.
 *
 * @property repository
 * @property dataStoreRepository
 * @constructor Create [TasksStore]
 *
 * @param initialState
 * @param reducer
 */
class TasksStore @Inject constructor(
    private val repository: IToDoRepository,
    private val dataStoreRepository: IDataStoreRepository, initialState: TaskViewState,
    reducer: Reducer<TaskViewState, TasksActions>,
) : BaseStore<TaskViewState, TasksActions>(
    initialState = TaskViewState(),
    reducer = TaskReducer(),
    middlewares = listOf(
        TaskMiddleware(
            repository = repository,
            dataStoreRepository = dataStoreRepository
        )
    )
)