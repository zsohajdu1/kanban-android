package com.example.kanban_android.presentation.board

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kanban_android.compose.ActionDialog
import com.example.kanban_android.compose.BasicScreen
import com.example.kanban_android.compose.ConfimDialog
import com.example.kanban_android.compose.SelectableLazyColumn
import com.example.kanban_android.domain.model.Board
import com.example.kanban_android.domain.model.Column
import com.example.kanban_android.domain.model.Task

@Composable
fun BoardScreen(
    openScreen: (String) -> Unit,
    navigateBack: () -> Unit,
    viewModel: BoardViewModel = hiltViewModel(),
    board: Board
) {
    LaunchedEffect(true) {
        viewModel.init(board)
    }
    val uiState by viewModel.uiState
    BoardScreenContent(
        uiState = uiState,
        tasksOfColumn = viewModel::tasksOfColumn,
        onColumnCreation = viewModel::onColumnCreation,
        onColumnCreationChange = viewModel::onColumnCreationChange,
        onNewColumnNameChange = viewModel::onNewColumnNameChange,
        onRenamingChange = viewModel::onRenamingChange,
        onRenaming = viewModel::onRenaming,
        onNewBoardNameChange = viewModel::onNewBoardNameChange,
        onTaskCreation = viewModel::onTaskCreation,
        onTaskCreationChange = viewModel::onTaskCreationChange,
        onNewTaskNameChange = viewModel::onNewTaskNameChange,
        onDeletingColumnChange = viewModel::onDeletingColumnChange,
        onDeletingChange = viewModel::onDeletingChange,
        onDelete = { viewModel.onDelete(navigateBack) },
        onRenamingColumn = viewModel::onRenamingColumn,
        onRenamingColumnNameChange = viewModel::onRenamingColumnNameChange,
        onRenamingColumnChange = viewModel::onRenamingColumnChange,
        onDeletingColumn = viewModel::onDeletingColumn,
        onTaskSelected = { viewModel.onTaskSelected(it, openScreen) },
    )
}

@Composable
fun BoardScreenContent(
    modifier: Modifier = Modifier,
    uiState: BoardViewModel.BoardUiState,
    tasksOfColumn: (Column) -> List<Task>,
    onColumnCreation: () -> Unit,
    onColumnCreationChange: (Boolean) -> Unit,
    onNewColumnNameChange: (String) -> Unit,
    onRenamingChange: (Boolean) -> Unit,
    onRenaming: () -> Unit,
    onNewBoardNameChange: (String) -> Unit,
    onTaskCreation: (Column) -> Unit,
    onTaskCreationChange: (Boolean) -> Unit,
    onNewTaskNameChange: (String) -> Unit,
    onDeletingColumnChange: (Boolean) -> Unit,
    onDeletingChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onRenamingColumn: (Column) -> Unit,
    onRenamingColumnNameChange: (String) -> Unit,
    onRenamingColumnChange: (Boolean) -> Unit,
    onDeletingColumn: (Column) -> Unit,
    onTaskSelected: (Task) -> Unit
) {
    uiState.board?.let {
        BasicScreen(
            topAppTitle = it.name,
            topAppActions = {
                IconButton(onClick = { onColumnCreationChange(true) }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Create Column",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
                IconButton(onClick = { onRenamingChange(true) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Rename Column",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
                IconButton(onClick = { onDeletingChange(true) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Column",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            })
        { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(innerPadding)
            ) {
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(uiState.columns) { column ->
                        ColumnCard(
                            uiState = uiState,
                            modifier = modifier,
                            column = column,
                            tasks = tasksOfColumn(column),
                            onTaskCreation = { onTaskCreation(column) },
                            onTaskCreationChange = onTaskCreationChange,
                            onNewTaskNameChange = onNewTaskNameChange,
                            onRenamingColumn = { onRenamingColumn(column) },
                            onDeletingColumnChange = onDeletingColumnChange,
                            onRenamingColumnNameChange = onRenamingColumnNameChange,
                            onRenamingColumnChange = onRenamingColumnChange,
                            onDeletingColumn = { onDeletingColumn(column) },
                            onTaskSelected = onTaskSelected
                        )
                    }
                }
            }
            when {
                uiState.columnCreation -> {
                    ActionDialog(
                        modifier = modifier,
                        value = uiState.newColumnName,
                        onNameChange = onNewColumnNameChange,
                        onValueCreation = onColumnCreation,
                        onValueCreationChanged = onColumnCreationChange,
                        valueIditentifier = "Column name"
                    )
                }

                uiState.renaming -> {
                    ActionDialog(
                        modifier = modifier,
                        value = uiState.newBoardName,
                        onNameChange = onNewBoardNameChange,
                        onValueCreation = onRenaming,
                        onValueCreationChanged = onRenamingChange,
                        valueIditentifier = "New Board Name",
                        actionName = "Rename"
                    )
                }

                uiState.deleting -> {
                    ConfimDialog(
                        modifier = modifier,
                        onConfirmation = onDelete,
                        onConfirmationChanged = onDeletingChange,
                        confirmationText = "Are you sure you want to delete this board, and all its columns and tasks?",
                        confirmButtonText = "Delete"
                    )
                }


            }
        }
    }
}

@Composable
fun ColumnCard(
    uiState: BoardViewModel.BoardUiState,
    modifier: Modifier = Modifier,
    column: Column,
    tasks: List<Task>,
    onTaskSelected: (Task) -> Unit,
    onTaskCreation: () -> Unit,
    onTaskCreationChange: (Boolean) -> Unit,
    onNewTaskNameChange: (String) -> Unit,
    onDeletingColumnChange: (Boolean) -> Unit,
    onRenamingColumn: () -> Unit,
    onRenamingColumnNameChange: (String) -> Unit,
    onRenamingColumnChange: (Boolean) -> Unit,
    onDeletingColumn: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxHeight()
            .width(200.dp),
        border = CardDefaults.outlinedCardBorder(),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Row(
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                modifier = modifier.padding(10.dp),
                text = column.name
            )
            IconButton(
                onClick = { expanded = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Rename") },
                    onClick = { onRenamingColumnChange(true) },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = null
                        )
                    })
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = { onDeletingColumnChange(true) },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = null
                        )
                    })

                DropdownMenuItem(
                    text = { Text("Add Task") },
                    onClick = { onTaskCreationChange(true) },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = null
                        )
                    })
            }

        }
        SelectableLazyColumn(
            modifier = modifier,
            items = tasks,
            onItemSelected = onTaskSelected
        )
    }
    when {
        uiState.taskCreation && expanded -> {
            ActionDialog(
                modifier = modifier,
                value = uiState.newTaskName,
                onNameChange = onNewTaskNameChange,
                onValueCreation = {
                    onTaskCreation()
                    expanded = false
                },
                onValueCreationChanged = onTaskCreationChange,
                valueIditentifier = "Task name"
            )
        }

        uiState.columnRenaming && expanded -> {
            ActionDialog(
                modifier = modifier,
                value = uiState.renamingColumnName,
                onNameChange = onRenamingColumnNameChange,
                onValueCreation = {
                    onRenamingColumn()
                    expanded = false
                },
                onValueCreationChanged = onRenamingColumnChange,
                valueIditentifier = "New Column name",
                actionName = "Rename"
            )
        }

        uiState.columnDeleting && expanded -> {
            ConfimDialog(
                modifier = modifier,
                onConfirmation = {
                    onDeletingColumn()
                    expanded = false
                },
                onConfirmationChanged = onDeletingColumnChange,
                confirmationText = "Are you sure you want to delete this column, and all its tasks?",
                confirmButtonText = "Delete"
            )

        }
    }
}