package com.example.kanban_android.presentation.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kanban_android.compose.BasicScreen
import com.example.kanban_android.domain.model.Board
import com.example.kanban_android.domain.model.Column
import com.example.kanban_android.domain.model.Task

@Composable
fun TaskScreen(
    navigateBack: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel(),
    board: Board,
    task: Task
) {
    LaunchedEffect(true) {
        viewModel.init(task, board)
    }
    val uiState by viewModel.uiState

    TaskScreenContent(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onDesciprtionChange = viewModel::onDescriptionChange,
        onColumnChange = viewModel::onColumnChange,
        onSaveChanges = viewModel::onSaveChanges,
        onDeleteTask = { viewModel.onDeleteTask(navigateBack) }
    )
}

@Composable
fun TaskScreenContent(
    modifier: Modifier = Modifier,
    uiState: TaskViewModel.TaskUiState,
    onNameChange: (String) -> Unit,
    onDesciprtionChange: (String) -> Unit,
    onColumnChange: (Column) -> Unit,
    onSaveChanges: () -> Unit,
    onDeleteTask: () -> Unit
) {
    BasicScreen(
        topAppTitle = "Edit task"
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(60.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    modifier = modifier.fillMaxWidth(),
                    singleLine = true,
                    value = uiState.taskName,
                    onValueChange = onNameChange,
                    label = { Text(text = "Name") }
                )
                TextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    value = uiState.taskDescription,
                    onValueChange = onDesciprtionChange,
                    label = { Text(text = "Description") }
                )
                Text(text = "Change column:")
                LazyColumn(
                    modifier.selectableGroup(),
                    horizontalAlignment = Alignment.Start
                ) {
                    items(uiState.columns) { column ->
                        Row() {
                            RadioButton(
                                selected = column.id == (uiState.selectedColumn?.id ?: false),
                                onClick = { onColumnChange(column) }
                            )
                            Text(text = column.name)
                        }
                    }
                }
                Button(
                    modifier = modifier.fillMaxWidth(),
                    onClick = onSaveChanges
                ) {
                    Text("Save")
                }
                Button(
                    modifier = modifier.fillMaxWidth(),
                    onClick = onDeleteTask,
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete task")
                }
            }
        }
    }

}