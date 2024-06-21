package com.example.kanban_android.domain.repositories.impl

import com.example.kanban_android.domain.model.Board
import com.example.kanban_android.domain.model.Workspace
import com.example.kanban_android.domain.repositories.BOARDS
import com.example.kanban_android.domain.repositories.WORKSPACES
import com.example.kanban_android.domain.repositories.base.BoardRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BoardRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : BoardRepository {
    override fun getBoardsOfWorkspace(workspace: Workspace): Flow<List<Board>> = flow {
        try {
            val boardsCollection = firebaseFirestore.collection(WORKSPACES).document(workspace.id)
                .collection(BOARDS)
            val boardsRef = boardsCollection.get().await()
            val boards = boardsRef.documents.map { boardDocument ->
                Board(
                    id = boardDocument.id,
                    name = boardDocument.getString("name") ?: "",
                    workspaceId = workspace.id
                )
            }
            emit(boards)
        } catch (e: Exception) {
            throw Exception("Can't get workspaces")
        }
    }

    override suspend fun createBoard(name: String, workspace: Workspace): Board {
        return try {
            val workspaceRef = firebaseFirestore.collection(WORKSPACES).document(workspace.id)
            val data = hashMapOf(
                "name" to name,
            )
            val boardRef = workspaceRef.collection(BOARDS).add(data).await()
            val boardSnapshot = boardRef.get().await()
            Board(
                id = boardSnapshot.id,
                name = boardSnapshot.getString("name") ?: "",
                workspaceId = workspace.id
            )
        } catch (e: Exception) {
            throw Exception("Board creation failed")
        }
    }

    override suspend fun deleteBoard(board: Board) {
        try {
            firebaseFirestore.collection(WORKSPACES).document(board.workspaceId)
                .collection(BOARDS).document(board.id)
                .delete()
        } catch (e: Exception) {
            throw Exception("Board deletion failed")
        }
    }

    override suspend fun renameBoard(board: Board, newName: String): Board {
        return try {
            val data = mapOf(
                "name" to newName,
            )
            firebaseFirestore.collection(WORKSPACES).document(board.workspaceId)
                .collection(BOARDS).document(board.id)
                .update(data)
            Board(
                id = board.id,
                name = newName,
                workspaceId = board.workspaceId
            )
        } catch (e: Exception) {
            throw Exception("Board renaming failed")
        }
    }
}