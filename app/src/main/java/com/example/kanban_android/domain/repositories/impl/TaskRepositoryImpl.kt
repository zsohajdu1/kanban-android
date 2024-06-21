package com.example.kanban_android.domain.repositories.impl

import com.example.kanban_android.domain.model.Column
import com.example.kanban_android.domain.model.Task
import com.example.kanban_android.domain.repositories.BOARDS
import com.example.kanban_android.domain.repositories.COLUMNS
import com.example.kanban_android.domain.repositories.TASKS
import com.example.kanban_android.domain.repositories.WORKSPACES
import com.example.kanban_android.domain.repositories.base.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : TaskRepository {

    override fun getTasksOfColumn(column: Column): Flow<List<Task>> = flow {
        try {
            val taskCollection =
                firebaseFirestore.collection(WORKSPACES).document(column.workspaceId)
                    .collection(BOARDS).document(column.boardId)
                    .collection(COLUMNS).document(column.id)
                    .collection(TASKS)
            val tasksRef = taskCollection.get().await()
            val tasks = tasksRef.documents.map { taskDocument ->
                Task(
                    id = taskDocument.id,
                    name = taskDocument.getString("name") ?: "",
                    description = taskDocument.getString("description") ?: "",
                    workspaceId = column.workspaceId,
                    boardId = column.boardId,
                    columnId = column.id
                )
            }
            emit(tasks)
        } catch (e: Exception) {
            throw Exception("Failed to get tasks of column")
        }
    }


    override suspend fun createTask(name: String, column: Column): Task {
        return try {
            val columnRef = firebaseFirestore.collection(WORKSPACES).document(column.workspaceId)
                .collection(BOARDS).document(column.boardId)
                .collection(COLUMNS).document(column.id)
            val data = hashMapOf(
                "name" to name,
            )
            val taskRef = columnRef.collection(TASKS).add(data).await()
            val taskSnapshot = taskRef.get().await()
            Task(
                id = taskSnapshot.id,
                name = taskSnapshot.getString("name") ?: "",
                workspaceId = column.workspaceId,
                boardId = column.boardId,
                columnId = column.id
            )
        } catch (e: Exception) {
            throw Exception("Error creating task")
        }
    }

    override suspend fun deleteTask(task: Task) {
        try {
            firebaseFirestore.collection(WORKSPACES).document(task.workspaceId)
                .collection(BOARDS).document(task.boardId)
                .collection(COLUMNS).document(task.columnId)
                .collection(TASKS).document(task.id)
                .delete()
        } catch (e: Exception) {
            throw Exception("Error deleting task")
        }
    }

    override suspend fun updateTask(task: Task) {
        try {
            val data = mapOf(
                "name" to task.name,
                "description" to task.description,
                "columnId" to task.columnId
            )
            firebaseFirestore.collection(WORKSPACES).document(task.workspaceId)
                .collection(BOARDS).document(task.boardId)
                .collection(COLUMNS).document(task.columnId)
                .collection(TASKS).document(task.id)
                .update(data)
        } catch (e: Exception) {
            throw Exception("Error updating task")
        }
    }

    override suspend fun renameTask(task: Task, newName: String): Task {
        return try {
            val data = mapOf(
                "name" to newName,
            )
            firebaseFirestore.collection(WORKSPACES).document(task.workspaceId)
                .collection(BOARDS).document(task.boardId)
                .collection(COLUMNS).document(task.columnId)
                .collection(TASKS).document(task.id)
                .update(data)
            Task(
                id = task.id,
                name = newName,
                description = task.description,
                workspaceId = task.workspaceId,
                boardId = task.boardId,
                columnId = task.columnId
            )
        } catch (e: Exception) {
            throw Exception("Error renaming task")
        }
    }

    override suspend fun renameDescription(task: Task, newDescription: String): Task {
        return try {
            val data = mapOf(
                "description" to newDescription,
            )
            firebaseFirestore.collection(WORKSPACES).document(task.workspaceId)
                .collection(BOARDS).document(task.boardId)
                .collection(COLUMNS).document(task.columnId)
                .collection(TASKS).document(task.id)
                .update(data)
            Task(
                id = task.id,
                name = task.name,
                description = task.description,
                workspaceId = task.workspaceId,
                boardId = task.boardId,
                columnId = task.columnId
            )
        } catch (e: Exception) {
            throw Exception("Error renaming task")
        }
    }

    override suspend fun moveTask(task: Task, newColumn: Column): Task {
        return try {
            deleteTask(task)
            createTask(task.name, newColumn)
        } catch (e: Exception) {
            throw Exception("Error moving task")
        }
    }
}