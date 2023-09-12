package com.example.to_docompose.di

import android.content.Context
import androidx.room.Room
import com.example.to_docompose.data.repositories.interfaces.IDataStoreRepository
import com.example.to_docompose.data.repositories.interfaces.IToDoRepository
import com.example.to_docompose.domain.TaskReducer
import com.example.to_docompose.domain.TasksStore
import com.example.to_docompose.domain.ToDoDao
import com.example.to_docompose.domain.ToDoDatabase
import com.example.to_docompose.domain.models.TaskViewState
import com.example.to_docompose.domain.repositories.DataStoreRepository
import com.example.to_docompose.domain.repositories.ToDoRepository
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
    fun providesTaskReducer() = TaskReducer()

    @Singleton
    @Provides
    fun provideTaskViewState(): TaskViewState {
        return TaskViewState()
    }
    @Singleton
    @Provides
    fun provideTasksStore(
        repository: ToDoRepository,
        dataStoreRepository: DataStoreRepository,
        reducer: TaskReducer,
    ) = TasksStore(
        initialState = TaskViewState(),
        repository = repository,
        dataStoreRepository = dataStoreRepository,
        reducer = reducer
    )

    @Singleton
    @Provides
    fun provideToDoRepository(toDoDao: ToDoDao): IToDoRepository = ToDoRepository(toDoDao = toDoDao)


    @Singleton
    @Provides
    fun provideDataStoreRepository(  @ApplicationContext context: Context): IDataStoreRepository = DataStoreRepository(context = context)
}
