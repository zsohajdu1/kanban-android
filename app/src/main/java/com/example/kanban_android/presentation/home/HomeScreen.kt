package com.example.kanban_android.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
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
import com.example.kanban_android.domain.model.Workspace

@Composable
fun HomeScreen(
    openScreen: (String) -> Unit,
    openScreenOnTop: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        viewModel.init()
    }
    val uiState by viewModel.uiState
    HomeScreenContent(
        uiState = uiState,
        onWorkspaceSelected = { viewModel.onWorkspaceSelected(it, openScreen) },
        onWorkspaceCreation = { viewModel.onWorkspaceCreation(openScreen) },
        onNewWorkspaceChange = viewModel::onNewWorkspaceChange,
        onWorkspaceCreationChanged = viewModel::onWorkspaceCreationChange,
        onSignOut = { viewModel.signOut(openScreenOnTop) },
        onSignOutRequestChange = viewModel::onSignOutRequestChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    uiState: HomeViewModel.HomeUiState,
    onWorkspaceSelected: (Workspace) -> Unit,
    onWorkspaceCreation: () -> Unit,
    onWorkspaceCreationChanged: (Boolean) -> Unit,
    onNewWorkspaceChange: (String) -> Unit,
    onSignOut: () -> Unit,
    onSignOutRequestChange: (Boolean) -> Unit
) {
    BasicScreen(
        topAppTitle = "Workspaces",
        topAppActions = {
            IconButton(onClick = { onWorkspaceCreationChanged(true) }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Create Workspace",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
            IconButton(onClick = { onSignOutRequestChange(true) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Sign out",
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
                items = uiState.workspaces,
                onItemSelected = onWorkspaceSelected
            )

            when {
                uiState.workspaceCreation -> {
                    ActionDialog(
                        modifier = modifier,
                        value = uiState.newWorkspaceName,
                        onNameChange = onNewWorkspaceChange,
                        onValueCreation = onWorkspaceCreation,
                        onValueCreationChanged = onWorkspaceCreationChanged,
                        valueIditentifier = "Workspace name"
                    )
                }

                uiState.signOutRequest -> {
                    ConfimDialog(
                        modifier = modifier,
                        onConfirmation = onSignOut,
                        onConfirmationChanged = onSignOutRequestChange,
                        confirmationText = "Are you sure you want to sign out?",
                        confirmButtonText = "Sign out"
                    )
                }
            }
        }
    }
}
