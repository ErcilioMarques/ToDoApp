package com.example.to_docompose.data.repositories.interfaces

import com.example.to_docompose.data.models.Priority
import kotlinx.coroutines.flow.Flow

interface IDataStoreRepository {
    private object PreferenceKeys
    suspend fun persistSortState(priority: Priority)

    val readSortState: Flow<String>
}