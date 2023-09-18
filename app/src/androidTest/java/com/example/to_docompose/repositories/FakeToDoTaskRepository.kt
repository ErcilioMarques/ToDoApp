package com.example.to_docompose.repositories

import com.example.to_docompose.data.repositories.interfaces.IToDoRepository
import com.example.to_docompose.domain.models.Priority
import com.example.to_docompose.domain.models.ToDoTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeToDoTaskRepository : IToDoRepository {
    private val tasks = mutableListOf<ToDoTask>(
        ToDoTask(title = "title2", description = "description2", priority = Priority.LOW)
    )

    override val getAllTasks: Flow<List<ToDoTask>> = flow {
        emit(tasks)
    }

    override val sortByLowPriority: Flow<List<ToDoTask>> = flow {
        val sortedTasks = tasks.sortedWith(compareByDescending<ToDoTask> {
            // Here, you can specify the priority you want to come first.
            // For example, if you want HIGH priority tasks to come first:
            when (it.priority) {
                Priority.LOW -> 1
                else -> 0
            }
        }.thenBy { it.priority }) // Then sort by priority in ascending order for the remaining tasks
        emit(sortedTasks)
    }

    override val sortByHighPriority: Flow<List<ToDoTask>> = flow {
        val sortedTasks = tasks.sortedWith(compareByDescending<ToDoTask> {
            // Here, you can specify the priority you want to come first.
            // For example, if you want HIGH priority tasks to come first:
            when (it.priority) {
                Priority.HIGH -> 1
                else -> 0
            }
        }.thenBy { it.priority }) // Then sort by priority in ascending order for the remaining tasks
        emit(sortedTasks)
    }

    override fun getSelectedTask(taskId: Int): Flow<ToDoTask> = flow {
        val selectedTask = tasks.find { it.id == taskId }
        if (selectedTask != null) {
            emit(selectedTask)
        }
    }

    override suspend fun addTask(toDoTask: ToDoTask) {
        if (toDoTask.title.isNotEmpty() && toDoTask.description.isNotEmpty())
            tasks.add(toDoTask.copy(id = tasks.size + 1))

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
        val matchingTasks = tasks.filter { it.title.contains(searchQuery.replace("%", ""), ignoreCase = true) }
        emit(matchingTasks)
    }
}