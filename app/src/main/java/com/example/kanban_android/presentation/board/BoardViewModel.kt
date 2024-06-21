package com.example.kanban_android.presentation.board

import androidx.compose.runtime.mutableStateOf
import com.example.kanban_android.TASK_SCREEN
import com.example.kanban_android.R.string as AppText
import com.example.kanban_android.common.snackbar.SnackbarManager
import com.example.kanban_android.domain.model.Board
import com.example.kanban_android.domain.model.Column
import com.example.kanban_android.domain.model.Task
import com.example.kanban_android.domain.services.base.AuthService
import com.example.kanban_android.domain.repositories.base.BoardRepository
import com.example.kanban_android.domain.repositories.base.ColumnRepository
import com.example.kanban_android.domain.services.base.LogService
import com.example.kanban_android.domain.repositories.base.TaskRepository
import com.example.kanban_android.presentation.KanbanAndroidViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val authService: AuthService,
    private val logService: LogService,
    private val boardRepository: BoardRepository,
    private val columnRepository: ColumnRepository,
    private val taskRepository: TaskRepository,
) : KanbanAndroidViewModel(logService) {

    val uiState = mutableStateOf(BoardUiState())

    data class BoardUiState(
        val board: Board? = null,
        val columns: List<Column> = emptyList(),
        val tasks: Map<Column, List<Task>> = emptyMap(),
        val columnCreation: Boolean = false,
        val taskCreation: Boolean = false,
        val newColumnName: String = "",
        val newBoardName: String = "",
        val newTaskName: String = "",
        val renaming: Boolean = false,
        val deleting: Boolean = false,
        val columnRenaming: Boolean = false,
        val columnDeleting: Boolean = false,
        val renamingColumnName: String = ""
    ) {
    }

    fun init(board: Board) {
        uiState.value = uiState.value.copy(board = board)
        launchCatching {
            columnRepository.getColumnsOfBoard(board).collect { columns ->
                uiState.value = uiState.value.copy(columns = columns)
                columns.forEach { column ->
                    launchCatching {
                        taskRepository.getTasksOfColumn(column).collect { tasks ->
                            uiState.value =
                                uiState.value.copy(tasks = uiState.value.tasks + (column to tasks))
                        }
                    }
                }
            }
        }
    }

    fun onColumnCreation() {
        val newColumnName = uiState.value.newColumnName
        if (newColumnName.isBlank()) {
            SnackbarManager.showMessage(AppText.name_empty_error)
        } else launchCatching {
            val column = columnRepository.createColumn(
                newColumnName,
                uiState.value.board!!
            )
            uiState.value = uiState.value.copy(columns = uiState.value.columns + column)
            uiState.value =
                uiState.value.copy(tasks = uiState.value.tasks + (column to emptyList()))
        }
    }

    fun onColumnCreationChange(newValue: Boolean) {
        uiState.value = uiState.value.copy(columnCreation = newValue)
    }

    fun onNewColumnNameChange(newValue: String) {
        uiState.value = uiState.value.copy(newColumnName = newValue)
    }

    fun onTaskCreation(column: Column) {
        val newTaskName = uiState.value.newTaskName
        if (newTaskName.isBlank()) {
            SnackbarManager.showMessage(AppText.name_empty_error)
        } else launchCatching {
            val task = taskRepository.createTask(
                newTaskName,
                column
            )
            uiState.value =
                uiState.value.copy(tasks = uiState.value.tasks.mapValues {
                    if (it.key == column) {
                        it.value + task
                    } else {
                        it.value
                    }
                })
        }
    }

    fun onTaskCreationChange(newValue: Boolean) {
        uiState.value = uiState.value.copy(taskCreation = newValue)
    }

    fun onNewTaskNameChange(newValue: String) {
        uiState.value = uiState.value.copy(newTaskName = newValue)
    }

    fun tasksOfColumn(column: Column): List<Task> {
        return uiState.value.tasks[column] ?: emptyList()
    }

    fun onRenaming() {
        val newBoardName = uiState.value.newBoardName
        if (newBoardName.isBlank()) {
            SnackbarManager.showMessage(AppText.name_empty_error)
        } else launchCatching {
            val board = boardRepository.renameBoard(
                uiState.value.board!!,
                uiState.value.newBoardName
            )
            uiState.value = uiState.value.copy(board = board)
        }
    }

    fun onRenamingColumn(column: Column) {
        val renamingColumnName = uiState.value.renamingColumnName
        if (renamingColumnName.isBlank()) {
            SnackbarManager.showMessage(AppText.name_empty_error)
        } else launchCatching {
            val renamedColumn = columnRepository.renameColumn(
                column,
                renamingColumnName
            )
            uiState.value = uiState.value.copy(columns = uiState.value.columns.map {
                if (it == column) {
                    renamedColumn
                } else {
                    it
                }
            })
            uiState.value = uiState.value.copy(tasks = uiState.value.tasks.mapKeys{
                if (it.key == column) {
                    renamedColumn
                } else {
                    it.key
                }
            }
            )
        }
    }

    fun onNewBoardNameChange(newValue: String) {
        uiState.value = uiState.value.copy(newBoardName = newValue)
    }
    fun onRenamingChange(newValue: Boolean) {
        uiState.value = uiState.value.copy(renaming = newValue)
    }

    fun onDeletingChange(newValue: Boolean) {
        uiState.value = uiState.value.copy(deleting = newValue)
    }
    fun onRenamingColumnNameChange(newValue: String) {
        uiState.value = uiState.value.copy(renamingColumnName = newValue)
    }
    fun onRenamingColumnChange(newValue: Boolean) {
        uiState.value = uiState.value.copy(columnRenaming = newValue)
    }

    fun onDeletingColumnChange(newValue: Boolean) {
        uiState.value = uiState.value.copy(columnDeleting = newValue)

    }

    fun onDeletingColumn(column: Column) {
        launchCatching {
            columnRepository.deleteColumn(column)
            uiState.value = uiState.value.copy(columns = uiState.value.columns - column)
        }
    }

    fun onTaskSelected(task: Task, openScreen: (String) -> Unit) {
        val gson = Gson()
        val boardJson = gson.toJson(uiState.value.board)
        val taskJson = gson.toJson(task)
        openScreen("$TASK_SCREEN/$boardJson/$taskJson")
    }

    fun onDelete(navigateBack: () -> Unit) {
        val board = uiState.value.board
        if (board != null) {
            launchCatching {
                boardRepository.deleteBoard(board)
                navigateBack()
            }
        }
    }

}