package com.example.kanban_android.domain.repositories.base

import com.example.kanban_android.domain.model.Board
import com.example.kanban_android.domain.model.Workspace
import kotlinx.coroutines.flow.Flow

interface BoardRepository {
    fun getBoardsOfWorkspace(workspace: Workspace): Flow<List<Board>>
    suspend fun createBoard(name: String, workspace: Workspace): Board
    suspend fun deleteBoard(board: Board)
    suspend fun renameBoard(board: Board, newName: String): Board
}