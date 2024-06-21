package com.example.kanban_android.presentation.workspace

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kanban_android.compose.BasicScreen
import com.example.kanban_android.compose.ActionDialog
import com.example.kanban_android.compose.ConfimDialog
import com.example.kanban_android.compose.SelectableLazyColumn
import com.example.kanban_android.domain.model.Board
import com.example.kanban_android.domain.model.NamedData
import com.example.kanban_android.domain.model.Workspace

@Composable
fun WorkspaceScreen(
    openScreen: (String) -> Unit,
    navigateBack: () -> Unit,
    viewModel: WorkspaceViewModel = hiltViewModel(),
    workspace: Workspace
) {
    LaunchedEffect(true) {
        viewModel.init(workspace)
    }
    val uiState by viewModel.uiState
    WorkspaceScreenContent(
        uiState = uiState,
        onBoardSelected = { viewModel.onBoardSelected(it, openScreen) },

        onNewBoardChange = viewModel::onNewBoardChange,
        onBoardCreation = { viewModel.onBoardCreation(openScreen) },
        onBoardCreationChanged = viewModel::onBoardCreationChanged,

        onUserEmailChange = viewModel::onUserEmailChange,
        onUserAdding = viewModel::onUserAdding,
        onUserAddingChanged = viewModel::onUserAddingChanged,

        newWorkspaceNameChange = viewModel::newWorkspaceNameChange,
        onRenaming = viewModel::onRenaming,
        onRenamingChanged = viewModel::onRenamingChanged,


        onDelete = { viewModel.onDelete(navigateBack) },
        onDeletingChange = viewModel::onDeletingChange

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkspaceScreenContent(
    modifier: Modifier = Modifier,
    uiState: WorkspaceViewModel.WorkspaceUiState,

    onBoardSelected: (Board) -> Unit,

    onNewBoardChange: (String) -> Unit,
    onBoardCreation: () -> Unit,
    onBoardCreationChanged: (Boolean) -> Unit,

    onUserEmailChange: (String) -> Unit,
    onUserAdding: () -> Unit,
    onUserAddingChanged: (Boolean) -> Unit,


    newWorkspaceNameChange: (String) -> Unit,
    onRenaming: () -> Unit,
    onRenamingChanged: (Boolean) -> Unit,

    onDelete: () -> Unit,
    onDeletingChange: (Boolean) -> Unit
) {
    uiState.workspace?.let {
        BasicScreen(
            topAppTitle = it.name,
            topAppActions = {
                IconButton(onClick = { onBoardCreationChanged(true) }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Create Board",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
                IconButton(onClick = { onUserAddingChanged(true) }) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Add User",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
                IconButton(onClick = { onRenamingChanged(true) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Rename Workspace",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
                IconButton(onClick = { onDeletingChange(true) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Workspace",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(innerPadding)
            ) {
                SelectableLazyColumn(
                    modifier = modifier,
                    items = uiState.boards,
                    onItemSelected = onBoardSelected,
                    title = "Boards"
                )

                when {
                    uiState.boardCreation -> {
                        ActionDialog(
                            modifier = modifier,
                            value = uiState.newBoardName,
                            onNameChange = onNewBoardChange,
                            onValueCreation = onBoardCreation,
                            onValueCreationChanged = onBoardCreationChanged,
                            valueIditentifier = "Board Name"
                        )
                    }

                    uiState.userAdding -> {
                        ActionDialog(
                            modifier = modifier,
                            value = uiState.userEmail,
                            onNameChange = onUserEmailChange,
                            onValueCreation = onUserAdding,
                            onValueCreationChanged = onUserAddingChanged,
                            valueIditentifier = "User Email",
                            actionName = "Add User"
                        )
                    }

                    uiState.renaming -> {
                        ActionDialog(
                            modifier = modifier,
                            value = uiState.newWorkspaceName,
                            onNameChange = newWorkspaceNameChange,
                            onValueCreation = onRenaming,
                            onValueCreationChanged = onRenamingChanged,
                            valueIditentifier = "New Workspace Name",
                            actionName = "Rename"
                        )
                    }

                    uiState.deleting -> {
                        ConfimDialog(
                            modifier = modifier,
                            onConfirmation = onDelete,
                            onConfirmationChanged = onDeletingChange,
                            confirmationText = "Are you sure you want to delete this workspace and all of its boards?",
                            confirmButtonText = "Delete"
                        )
                    }
                }
            }
        }
    }

}

