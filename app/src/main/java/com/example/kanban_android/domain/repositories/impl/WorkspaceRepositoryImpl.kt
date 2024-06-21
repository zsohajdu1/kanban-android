package com.example.kanban_android.domain.repositories.impl

import com.example.kanban_android.domain.model.User
import com.example.kanban_android.domain.model.Workspace
import com.example.kanban_android.domain.repositories.USERS_WORKSPACES
import com.example.kanban_android.domain.repositories.WORKSPACES
import com.example.kanban_android.domain.repositories.base.WorkspaceRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WorkspaceRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : WorkspaceRepository {
    override fun getWorkspacesOfUser(user: User): Flow<List<Workspace>> = flow {
        try {
            val usersWorkspacesCollection = firebaseFirestore.collection(USERS_WORKSPACES)
            val usersWorkspacesRef =
                usersWorkspacesCollection.whereEqualTo("userId", user.uid).get().await()
            val workspaceIds =
                usersWorkspacesRef.documents.map { it.getString("workspaceId") ?: "" }
            val workspacesCollection = firebaseFirestore.collection(WORKSPACES)
            val workspaces = workspaceIds.map { workspaceId ->
                val workspaceRef = workspacesCollection.document(workspaceId).get().await()
                Workspace(
                    id = workspaceRef.id,
                    name = workspaceRef.getString("name") ?: "Unnamed Workspace"
                )
            }
            emit(workspaces)
        } catch (e: Exception) {
            throw Exception("Error getting workspaces of user")
        }
    }

    override suspend fun createWorkspace(name: String, user: User): Workspace {
        return try {
            val workspaceCollection = firebaseFirestore.collection(WORKSPACES)
            val data = hashMapOf(
                "name" to name,
            )
            val workspaceRef = workspaceCollection.add(data).await()
            val workspaceSnapshot = workspaceRef.get().await()
            val createdWorkspace = Workspace(
                id = workspaceSnapshot.id,
                name = workspaceSnapshot.getString("name") ?: "Unnamed Workspace"
            )
            addUserToWorkspace(createdWorkspace, user)
            createdWorkspace
        } catch (e: Exception) {
            throw Exception("Error creating workspace")
        }
    }

    override suspend fun deleteWorkspace(workspace: Workspace) {
        try {
            firebaseFirestore.collection(WORKSPACES).document(workspace.id)
                .delete()
            firebaseFirestore.collection(USERS_WORKSPACES)
                .whereEqualTo("workspaceId", workspace.id).get().await().documents.forEach {
                    it.reference.delete()
                }
        } catch (e: Exception) {
            throw Exception("Error deleting workspace")
        }
    }

    override suspend fun renameWorkspace(workspace: Workspace, newName: String): Workspace {
        return try {
            val data = mapOf(
                "name" to newName,
            )
            firebaseFirestore.collection(WORKSPACES).document(workspace.id)
                .update(data)
            Workspace(
                id = workspace.id,
                name = newName
            )
        } catch (e: Exception) {
            throw Exception("Error renaming workspace")
        }
    }

    override suspend fun addUserToWorkspace(workspace: Workspace, user: User) {
        val data = hashMapOf(
            "userId" to user.uid,
            "workspaceId" to workspace.id
        )
        try {
            firebaseFirestore.collection(USERS_WORKSPACES).add(data).await()
        } catch (e: Exception) {
            throw Exception("Failed to add user to workspace")
        }
    }
}