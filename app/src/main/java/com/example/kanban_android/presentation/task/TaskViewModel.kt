package com.example.kanban_android.presentation.task

import androidx.compose.runtime.mutableStateOf
import com.example.kanban_android.BOARD_SCREEN
import com.example.kanban_android.R.string as AppText
import com.example.kanban_android.common.snackbar.SnackbarManager
import com.example.kanban_android.domain.model.Board
import com.example.kanban_android.domain.model.Column
import com.example.kanban_android.domain.model.Task
import com.example.kanban_android.domain.repositories.base.ColumnRepository
import com.example.kanban_android.domain.services.base.LogService
import com.example.kanban_android.domain.repositories.base.TaskRepository
import com.example.kanban_android.presentation.KanbanAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val logService: LogService,
    private val columnRepository: ColumnRepository,
    private val taskRepository: TaskRepository
) : KanbanAndroidViewModel(logService) {
    val uiState = mutableStateOf(TaskUiState())

    data class TaskUiState(
        val task: Task? = null,
        val columns: List<Column> = emptyList(),
        val selectedColumn: Column? = null,
        val taskName: String = "",
        val taskDescription: String = ""
    )

    fun init(task: Task, board: Board) {
        launchCatching {
            columnRepository.getColumnsOfBoard(board).collect { columns ->
                uiState.value = uiState.value.copy(
                    task = task,
                    columns = columns,
                    selectedColumn = columns.firstOrNull {
                        it.id == task.columnId
                    },
                    taskName = task.name
                )
            }
        }
    }

    fun onNameChange(newValue: String) {
        uiState.value = uiState.value.copy(taskName = newValue)
    }

    fun onDescriptionChange(newValue: String) {
        uiState.value = uiState.value.copy(taskDescription = newValue)
    }

    fun onColumnChange(newValue: Column) {
        uiState.value = uiState.value.copy(selectedColumn = newValue)
    }

    fun onSaveChanges() {
        val task = uiState.value.task
        val newTaskName = uiState.value.taskName
        val newTaskDescription = uiState.value.taskDescription
        val newSelectedColumn = uiState.value.selectedColumn
        if (task != null) {
            if (newTaskName.isBlank()) {
                SnackbarManager.showMessage(AppText.name_empty_error)
            } else if (newSelectedColumn == null) {
                SnackbarManager.showMessage(AppText.column_empty_error)
            } else launchCatching {
                val renamedTask = taskRepository.renameTask(task, newTaskName)
                val descRenamedTask = taskRepository.renameDescription(renamedTask, newTaskDescription)
                val movedTask = taskRepository.moveTask(descRenamedTask, newSelectedColumn)
                uiState.value = uiState.value.copy(task = movedTask)
                SnackbarManager.showMessage(AppText.task_updated_success)
            }
        }
    }

    fun onDeleteTask(navigateBack: () -> Unit) {
        val task = uiState.value.task
        if (task != null) {
            launchCatching {
                taskRepository.deleteTask(task)
                navigateBack()
            }
        }
    }
}