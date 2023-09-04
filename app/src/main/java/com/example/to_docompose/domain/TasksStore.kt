package com.example.to_docompose.domain

import com.example.to_docompose.domain.models.TaskViewState
import com.example.to_docompose.domain.repositories.DataStoreRepository
import com.example.to_docompose.domain.repositories.ToDoRepository
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
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository, initialState: TaskViewState,
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