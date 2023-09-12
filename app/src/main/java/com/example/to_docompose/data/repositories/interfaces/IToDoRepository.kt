package com.example.to_docompose.data.repositories.interfaces

import com.example.to_docompose.data.models.ToDoTask
import kotlinx.coroutines.flow.Flow

interface IToDoRepository {

    val getAllTasks: Flow<List<ToDoTask>>
    val sortByLowPriority: Flow<List<ToDoTask>>
    val sortByHighPriority: Flow<List<ToDoTask>>

    fun getSelectedTask(taskId: Int): Flow<ToDoTask>

    suspend fun addTask(toDoTask: ToDoTask)

    suspend fun updateTask(toDoTask: ToDoTask)

    suspend fun deleteTask(toDoTask: ToDoTask)

    suspend fun deleteAllTasks()

    fun searchDatabase(searchQuery: String): Flow<List<ToDoTask>>
}