package com.example.kanban_android.presentation.home

import androidx.compose.runtime.mutableStateOf
import com.example.kanban_android.AUTH_SCREEN
import com.example.kanban_android.R.string as AppText
import com.example.kanban_android.WORKSPACE_SCREEN
import com.example.kanban_android.common.snackbar.SnackbarManager
import com.example.kanban_android.domain.model.User
import com.example.kanban_android.domain.model.Workspace
import com.example.kanban_android.domain.services.base.AuthService
import com.example.kanban_android.domain.services.base.LogService
import com.example.kanban_android.domain.repositories.base.WorkspaceRepository
import com.example.kanban_android.presentation.KanbanAndroidViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authService: AuthService,
    private val logService: LogService,
    private val workspaceRepository: WorkspaceRepository
) : KanbanAndroidViewModel(logService) {

    val uiState = mutableStateOf(HomeUiState())

    data class HomeUiState(
        val workspaces: List<Workspace> = emptyList(),
        val workspaceCreation: Boolean = false,
        val signOutRequest: Boolean = false,
        val newWorkspaceName: String = "",
        val user: User? = null
    )

    fun signOut(openScreen: (String) -> Unit) {
        launchCatching {
            authService.signOut()
            openScreen(AUTH_SCREEN)
        }
    }

    fun init() {
        if (authService.hasUser)
            launchCatching {
                authService.currentUser.collect { user ->
                    uiState.value = uiState.value.copy(user = user)
                    workspaceRepository.getWorkspacesOfUser(user).collect { workspaces ->
                        uiState.value = uiState.value.copy(workspaces = workspaces)
                    }
                }
            }
    }


    fun onWorkspaceSelected(workspace: Workspace, openScreen: (String) -> Unit) {
        val gson = Gson()
        val workspaceJson = gson.toJson(workspace)
        openScreen("$WORKSPACE_SCREEN/$workspaceJson")
    }

    fun onWorkspaceCreation(openScreen: (String) -> Unit) {
        val newWorkspaceName = uiState.value.newWorkspaceName
        if (newWorkspaceName.isBlank()) {
            SnackbarManager.showMessage(AppText.name_empty_error)
        } else launchCatching {
            uiState.value.user?.let {
                val workspace = workspaceRepository.createWorkspace(newWorkspaceName, it)
                val gson = Gson()
                val workspaceJson = gson.toJson(workspace)
                uiState.value =
                    uiState.value.copy(workspaces = uiState.value.workspaces + workspace)
                openScreen("$WORKSPACE_SCREEN/$workspaceJson")
            }
        }
    }

    fun onNewWorkspaceChange(newValue: String) {
        uiState.value = uiState.value.copy(newWorkspaceName = newValue)
    }

    fun onWorkspaceCreationChange(newValue: Boolean) {
        uiState.value = uiState.value.copy(workspaceCreation = newValue)
    }

    fun onSignOutRequestChange(newValue: Boolean) {
        uiState.value = uiState.value.copy(signOutRequest = newValue)
    }
}