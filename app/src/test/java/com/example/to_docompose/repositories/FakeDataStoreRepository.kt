package com.example.to_docompose.repositories

import com.example.to_docompose.data.repositories.interfaces.IDataStoreRepository
import com.example.to_docompose.domain.models.Priority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDataStoreRepository: IDataStoreRepository {
    private var sortState: Priority = Priority.NONE

    override suspend fun persistSortState(priority: Priority) {
        sortState = priority
    }

    override val readSortState: Flow<String> = flow {
        val sortStateName = sortState.name
        emit(sortStateName)
    }
}