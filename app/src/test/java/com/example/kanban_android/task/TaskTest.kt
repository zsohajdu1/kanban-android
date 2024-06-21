package com.example.kanban_android.task

import com.example.kanban_android.MainDispatcherRule
import com.example.kanban_android.domain.model.Column
import com.example.kanban_android.domain.model.Task
import com.example.kanban_android.domain.repositories.base.ColumnRepository
import com.example.kanban_android.domain.repositories.base.TaskRepository
import com.example.kanban_android.domain.services.base.LogService
import com.example.kanban_android.presentation.board.BoardViewModel
import com.example.kanban_android.presentation.task.TaskViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class TaskTest {
    @get:Rule
    val composeTestRule = MainDispatcherRule()

    private var mockLogService = mockk<LogService>()
    private var mockColumnRepository = mockk<ColumnRepository>()
    private var mockTaskRepository = mockk<TaskRepository>()
    private lateinit var taskViewModel: TaskViewModel

    private var column = Column(
        id = "columnId",
        name = "columnName",
        workspaceId = "workspaceId",
        boardId = "boardId"
    )

    private var task = Task(
        id = "taskId",
        name = "taskName",
        workspaceId = "workspaceId",
        boardId = "boardId",
        columnId = "columnId",
        description = "taskDescription"

    )

    @Test
    fun deleteTask() {
        // Arrange
        coEvery { mockTaskRepository.deleteTask(task) } returns Unit

        taskViewModel = TaskViewModel(
            mockLogService,
            mockColumnRepository,
            mockTaskRepository
        )


        taskViewModel.uiState.value = taskViewModel.uiState.value.copy(task = task)

        // Act
        taskViewModel.onDeleteTask { }

        // Assert
    }

    @Test
    fun changeTaskEmptyName() {
        // Arrange

        taskViewModel = TaskViewModel(
            mockLogService,
            mockColumnRepository,
            mockTaskRepository
        )


        taskViewModel.uiState.value = taskViewModel.uiState.value.copy(task = task)
        taskViewModel.uiState.value = taskViewModel.uiState.value.copy(selectedColumn = column)

        // Act
        taskViewModel.onSaveChanges()

        // Assert
        assert(task == taskViewModel.uiState.value.task)
    }

    @Test
    fun changeTaskColumnNotSelected() {
        // Arrange

        taskViewModel = TaskViewModel(
            mockLogService,
            mockColumnRepository,
            mockTaskRepository
        )


        taskViewModel.uiState.value = taskViewModel.uiState.value.copy(task = task)
        taskViewModel.uiState.value = taskViewModel.uiState.value.copy(taskName = "newTaskName")

        // Act
        taskViewModel.onSaveChanges()

        // Assert
        assert(task == taskViewModel.uiState.value.task)

    }

    @Test
    fun renameTaskSuccess() {
        // Arrange
        val renamedTask = Task(
            id = "newTaskId",
            name = "newTaskName",
            workspaceId = "workspaceId",
            boardId = "boardId",
            columnId = "columnId",
            description = "taskDescription"
        )
        coEvery { mockTaskRepository.renameTask(task, "newTaskName") } returns renamedTask
        coEvery {
            mockTaskRepository.renameDescription(
                renamedTask,
                "taskDescription"
            )
        } returns renamedTask
        coEvery { mockTaskRepository.moveTask(renamedTask, column) } returns renamedTask

        taskViewModel = TaskViewModel(
            mockLogService,
            mockColumnRepository,
            mockTaskRepository
        )


        taskViewModel.uiState.value = taskViewModel.uiState.value.copy(task = task)
        taskViewModel.uiState.value = taskViewModel.uiState.value.copy(taskName = "newTaskName")
        taskViewModel.uiState.value =
            taskViewModel.uiState.value.copy(taskDescription = "taskDescription")
        taskViewModel.uiState.value = taskViewModel.uiState.value.copy(selectedColumn = column)

        // Act
        taskViewModel.onSaveChanges()

        // Assert
        assert(renamedTask == taskViewModel.uiState.value.task)
    }
}