package com.example.kanban_android.domain.repositories.base

import com.example.kanban_android.domain.model.Board
import com.example.kanban_android.domain.model.Column
import kotlinx.coroutines.flow.Flow

interface ColumnRepository {
    fun getColumnsOfBoard(board: Board): Flow<List<Column>>
    suspend fun createColumn(name: String, board: Board): Column
    suspend fun deleteColumn(column: Column)
    suspend fun renameColumn(column: Column, newName: String): Column
}