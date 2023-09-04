package com.example.to_docompose.domain

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.to_docompose.domain.models.ToDoTask

@Database(entities = [ToDoTask::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun todoDao(): ToDoDao
}
