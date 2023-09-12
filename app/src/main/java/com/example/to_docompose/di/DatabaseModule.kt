package com.example.to_docompose.di

import android.content.Context
import androidx.room.Room
import com.example.to_docompose.data.ToDoDao
import com.example.to_docompose.data.ToDoDatabase
import com.example.to_docompose.data.repositories.DataStoreRepository
import com.example.to_docompose.data.repositories.ToDoRepository
import com.example.to_docompose.data.repositories.interfaces.IDataStoreRepository
import com.example.to_docompose.data.repositories.interfaces.IToDoRepository
import com.example.to_docompose.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(
        context,
        ToDoDatabase::class.java,
        DATABASE_NAME,
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: ToDoDatabase) = database.todoDao()

    @Singleton
    @Provides
    fun provideToDoRepository(toDoDao: ToDoDao): IToDoRepository = ToDoRepository(toDoDao = toDoDao)


    @Singleton
    @Provides
    fun provideDataStoreRepository(  @ApplicationContext context: Context): IDataStoreRepository = DataStoreRepository(context = context)
}
