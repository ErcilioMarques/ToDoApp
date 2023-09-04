package com.example.to_docompose.domain

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.to_docompose.domain.models.ToDoTask
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAlltasks(): Flow<List<ToDoTask>>

    @Query("SELECT * FROM todo_table WHERE id=:taskId")
    fun getSelectedTask(taskId: Int): Flow<ToDoTask>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(toDoTask: ToDoTask)

    @Update
    suspend fun updatedTask(toDoTask: ToDoTask)

    @Delete
    suspend fun deleteTask(toDoTask: ToDoTask)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM todo_table WHERE title like :searchQuery OR description like :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<ToDoTask>>

    @Query(
        """SELECT * FROM todo_table ORDER BY 
    CASE
         WHEN priority LIKE 'L%' THEN 1
         WHEN priority LIKE 'M%' THEN 2
         WHEN priority LIKE 'H%' THEN 3
    END
    """
    )
    fun sortByLowPriority(): Flow<List<ToDoTask>>

    @Query(
        """SELECT * FROM todo_table ORDER BY
    CASE 
        WHEN priority LIKE 'H%' THEN 1
        WHEN priority LIKE 'M%' THEN 2
        WHEN priority LIKE 'L%' THEN 3 
    END
    """
    )
    fun sortByHighPriority(): Flow<List<ToDoTask>>
}
