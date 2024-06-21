package com.example.kanban_android.domain.repositories.base

import com.example.kanban_android.domain.model.User
import com.example.kanban_android.domain.model.Workspace
import kotlinx.coroutines.flow.Flow

interface WorkspaceRepository {
    fun getWorkspacesOfUser(user: User): Flow<List<Workspace>>
    suspend fun createWorkspace(name: String, user: User): Workspace
    suspend fun deleteWorkspace(workspace: Workspace)
    suspend fun renameWorkspace(workspace: Workspace, newName: String): Workspace
    suspend fun addUserToWorkspace(workspace: Workspace, user: User)
}