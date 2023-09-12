package com.example.to_docompose.data.repositories

import com.example.to_docompose.data.ToDoDao
import com.example.to_docompose.data.models.ToDoTask
import com.example.to_docompose.data.repositories.interfaces.IToDoRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ToDoRepository @Inject constructor(private val toDoDao: ToDoDao): IToDoRepository {

    override val getAllTasks: Flow<List<ToDoTask>> = toDoDao.getAlltasks()
    override val sortByLowPriority: Flow<List<ToDoTask>> = toDoDao.sortByLowPriority()
    override val sortByHighPriority: Flow<List<ToDoTask>> = toDoDao.sortByHighPriority()

    override fun getSelectedTask(taskId: Int): Flow<ToDoTask> {
        return toDoDao.getSelectedTask(taskId)
    }

    override suspend fun addTask(toDoTask: ToDoTask) {
        toDoDao.addTask(toDoTask)
    }

    override suspend fun updateTask(toDoTask: ToDoTask) {
        toDoDao.updatedTask(toDoTask)
    }

    override suspend fun deleteTask(toDoTask: ToDoTask) {
        toDoDao.deleteTask(toDoTask)
    }

    override suspend fun deleteAllTasks() {
        toDoDao.deleteAllTasks()
    }

    override fun searchDatabase(searchQuery: String): Flow<List<ToDoTask>> {
        return toDoDao.searchDatabase(searchQuery)
    }
}
