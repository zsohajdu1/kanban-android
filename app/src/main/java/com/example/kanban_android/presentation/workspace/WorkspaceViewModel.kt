package com.example.kanban_android.presentation.workspace

import androidx.compose.runtime.mutableStateOf
import com.example.kanban_android.BOARD_SCREEN
import com.example.kanban_android.R.string as AppText
import com.example.kanban_android.common.snackbar.SnackbarManager
import com.example.kanban_android.domain.model.Board
import com.example.kanban_android.domain.model.Workspace
import com.example.kanban_android.domain.services.base.AuthService
import com.example.kanban_android.domain.repositories.base.BoardRepository
import com.example.kanban_android.domain.repositories.base.ColumnRepository
import com.example.kanban_android.domain.services.base.LogService
import com.example.kanban_android.domain.repositories.base.WorkspaceRepository
import com.example.kanban_android.presentation.KanbanAndroidViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkspaceViewModel @Inject constructor(
    private val authService: AuthService,
    private val logService: LogService,
    private val workspaceRepository: WorkspaceRepository,
    private val boardRepository: BoardRepository,
    private val columnRepository: ColumnRepository
) : KanbanAndroidViewModel(logService) {

    val uiState = mutableStateOf(WorkspaceUiState())

    data class WorkspaceUiState(
        val workspace: Workspace? = null,
        val boards: List<Board> = emptyList(),
        val boardCreation: Boolean = false,
        val newBoardName: String = "",
        val renaming: Boolean = false,
        val deleting: Boolean = false,
        val userAdding: Boolean = false,
        val newWorkspaceName: String = "",
        val userEmail: String = ""
    )

    fun init(workspace: Workspace) {
        uiState.value = uiState.value.copy(workspace = workspace)
        launchCatching {
            boardRepository.getBoardsOfWorkspace(workspace).collect { boards ->
                uiState.value = uiState.value.copy(boards = boards)
            }
        }
    }

    fun onBoardSelected(board: Board, openScreen: (String) -> Unit) {
        val gson = Gson()
        val boardJson = gson.toJson(board)
        openScreen("$BOARD_SCREEN/$boardJson")
    }

    fun onBoardCreation(openScreen: (String) -> Unit) {
        val newBoardName = uiState.value.newBoardName
        if (newBoardName.isBlank()) {
            SnackbarManager.showMessage(AppText.name_empty_error)
        } else launchCatching {
            val board = boardRepository.createBoard(newBoardName, uiState.value.workspace!!)
            columnRepository.createColumn("TODO", board)
            columnRepository.createColumn("In Progress", board)
            columnRepository.createColumn("Done", board)
            val gson = Gson()
            val boardJson = gson.toJson(board)
            uiState.value = uiState.value.copy(boards = uiState.value.boards + board)
            openScreen("$BOARD_SCREEN/$boardJson")
        }

    }

    fun onNewBoardChange(newValue: String) {
        uiState.value = uiState.value.copy(newBoardName = newValue)
    }

    fun onBoardCreationChanged(newValue: Boolean) {
        uiState.value = uiState.value.copy(boardCreation = newValue)
    }

    fun onUserEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(userEmail = newValue)

    }

    fun onUserAdding() {
        val userEmail = uiState.value.userEmail
        if (userEmail.isBlank()) {
            SnackbarManager.showMessage(AppText.email_error)
        } else launchCatching {
            val user = authService.getUserByEmail(userEmail)
            workspaceRepository.addUserToWorkspace(uiState.value.workspace!!, user)
        }
    }

    fun onUserAddingChanged(newValue: Boolean) {
        uiState.value = uiState.value.copy(userAdding = newValue)
    }

    fun newWorkspaceNameChange(newValue: String) {
        uiState.value = uiState.value.copy(newWorkspaceName = newValue)
    }

    fun onRenaming() {
        val newWorkspaceName = uiState.value.newWorkspaceName
        if (newWorkspaceName.isBlank()) {
            SnackbarManager.showMessage(AppText.name_empty_error)
        } else launchCatching {
            val workspace = workspaceRepository.renameWorkspace(
                uiState.value.workspace!!,
                uiState.value.newWorkspaceName
            )
            uiState.value = uiState.value.copy(workspace = workspace)
        }
    }

    fun onRenamingChanged(newValue: Boolean) {
        uiState.value = uiState.value.copy(renaming = newValue)
    }

    fun onDeletingChange(newValue: Boolean) {
        uiState.value = uiState.value.copy(deleting = newValue)
    }

    fun onDelete(navigateBack: () -> Unit) {
        val workspace = uiState.value.workspace
        if (workspace != null) {
            launchCatching {
                workspaceRepository.deleteWorkspace(workspace)
                navigateBack()
            }
        }
    }


}