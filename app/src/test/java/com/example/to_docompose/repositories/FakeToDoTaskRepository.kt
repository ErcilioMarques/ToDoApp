package com.example.to_docompose.repositories

import com.example.to_docompose.data.repositories.interfaces.IToDoRepository
import com.example.to_docompose.domain.models.ToDoTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeToDoTaskRepository: IToDoRepository {
    private val tasks = mutableListOf<ToDoTask>()

    override val getAllTasks: Flow<List<ToDoTask>> = flow {
        emit(tasks)
    }

    override val sortByLowPriority: Flow<List<ToDoTask>> = flow {
        val sortedTasks = tasks.sortedBy { it.priority }
        emit(sortedTasks)
    }

    override val sortByHighPriority: Flow<List<ToDoTask>> = flow {
        val sortedTasks = tasks.sortedByDescending { it.priority }
        emit(sortedTasks)
    }

    override fun getSelectedTask(taskId: Int): Flow<ToDoTask> = flow {
        val selectedTask = tasks.find { it.id == taskId }
        if (selectedTask != null) {
            emit(selectedTask)
        }
    }

    override suspend fun addTask(toDoTask: ToDoTask) {
        if(toDoTask.title.isNotEmpty() && toDoTask.description.isNotEmpty())
        tasks.add(toDoTask.copy(id = 1))

    }

    override suspend fun updateTask(toDoTask: ToDoTask) {
        val index = tasks.indexOfFirst { it.id == toDoTask.id }
        if (index != -1) {
            tasks[index] = toDoTask
        }
    }

    override suspend fun deleteTask(toDoTask: ToDoTask) {
        tasks.removeIf { it.id == toDoTask.id }
    }

    override suspend fun deleteAllTasks() {
        tasks.clear()
    }

    override fun searchDatabase(searchQuery: String): Flow<List<ToDoTask>> = flow {
        val matchingTasks = tasks.filter { it.title.contains(searchQuery, ignoreCase = true) }
        emit(matchingTasks)
    }
}