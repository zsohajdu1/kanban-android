package com.example.kanban_android.domain.repositories.impl

import com.example.kanban_android.domain.model.Board
import com.example.kanban_android.domain.model.Column
import com.example.kanban_android.domain.repositories.BOARDS
import com.example.kanban_android.domain.repositories.COLUMNS
import com.example.kanban_android.domain.repositories.WORKSPACES
import com.example.kanban_android.domain.repositories.base.ColumnRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ColumnRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : ColumnRepository {
    override fun getColumnsOfBoard(board: Board): Flow<List<Column>> = flow {
        try {
            val columnsCollection =
                firebaseFirestore.collection(WORKSPACES).document(board.workspaceId)
                    .collection(BOARDS).document(board.id)
                    .collection(COLUMNS)
            val columnsRef = columnsCollection.get().await()
            val columns = columnsRef.documents.map { columnDocument ->
                Column(
                    id = columnDocument.id,
                    name = columnDocument.getString("name") ?: "",
                    workspaceId = board.workspaceId,
                    boardId = board.id
                )
            }
            emit(columns)
        } catch (e: Exception) {
            throw Exception("Error getting columns of board")
        }
    }

    override suspend fun createColumn(name: String, board: Board): Column {
        return try {
            val boardRef = firebaseFirestore.collection(WORKSPACES).document(board.workspaceId)
                .collection(BOARDS).document(board.id)
            val data = hashMapOf(
                "name" to name,
            )
            val columnRef = boardRef.collection(COLUMNS).add(data).await()
            val columnSnapshot = columnRef.get().await()
            Column(
                id = columnSnapshot.id,
                name = columnSnapshot.getString("name") ?: "",
                workspaceId = board.workspaceId,
                boardId = board.id
            )
        } catch (e: Exception) {
            throw Exception("Error creating column")
        }
    }

    override suspend fun deleteColumn(column: Column) {
        try {
            firebaseFirestore.collection(WORKSPACES).document(column.workspaceId)
                .collection(BOARDS).document(column.boardId)
                .collection(COLUMNS).document(column.id)
                .delete()
        } catch (e: Exception) {
            throw Exception("Error deleting column")
        }
    }

    override suspend fun renameColumn(column: Column, newName: String): Column {
        return try {
            val data = mapOf(
                "name" to newName,
            )
            firebaseFirestore.collection(WORKSPACES).document(column.workspaceId)
                .collection(BOARDS).document(column.boardId)
                .collection(COLUMNS).document(column.id)
                .update(data)
            Column(
                id = column.id,
                name = newName,
                workspaceId = column.workspaceId,
                boardId = column.boardId
            )
        } catch (e: Exception) {
            throw Exception("Error renaming column")
        }
    }
}