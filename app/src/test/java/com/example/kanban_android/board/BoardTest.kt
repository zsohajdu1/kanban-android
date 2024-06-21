package com.example.kanban_android.board

import com.example.kanban_android.MainDispatcherRule
import com.example.kanban_android.domain.model.Board
import com.example.kanban_android.domain.model.Column
import com.example.kanban_android.domain.model.Task
import com.example.kanban_android.domain.repositories.base.BoardRepository
import com.example.kanban_android.domain.repositories.base.ColumnRepository
import com.example.kanban_android.domain.repositories.base.TaskRepository
import com.example.kanban_android.domain.services.base.AuthService
import com.example.kanban_android.domain.services.base.LogService
import com.example.kanban_android.presentation.board.BoardViewModel
import com.example.kanban_android.presentation.workspace.WorkspaceViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class BoardTest {
    @get:Rule
    val composeTestRule = MainDispatcherRule()

    private var mockAuthService = mockk<AuthService>()
    private var mockLogService = mockk<LogService>()
    private var mockBoardRepository = mockk<BoardRepository>()
    private var mockColumnRepository = mockk<ColumnRepository>()
    private var mockTaskRepository = mockk<TaskRepository>()
    private lateinit var boardViewModel: BoardViewModel

    private var board: Board = Board(
        id = "boardId",
        name = "boardName",
        workspaceId = "workspaceId"
    )

    private var column = Column(
        id = "columnId",
        name = "columnName",
        workspaceId = "workspaceId",
        boardId = "boardId"
    )

    @Test
    fun deleteColumn() {
        // Arrange
        coEvery { mockColumnRepository.deleteColumn(column) } returns Unit

        boardViewModel = BoardViewModel(
            mockAuthService,
            mockLogService,
            mockBoardRepository,
            mockColumnRepository,
            mockTaskRepository
        )

        boardViewModel.uiState.value =
            boardViewModel.uiState.value.copy(columns = boardViewModel.uiState.value.columns + column)

        // Act
        boardViewModel.onDeletingColumn(column)

        // Assert
        assertEquals(0, boardViewModel.uiState.value.columns.size)
    }

    @Test
    fun createColumnEmptyName() {
        // Arrange
        boardViewModel = BoardViewModel(
            mockAuthService,
            mockLogService,
            mockBoardRepository,
            mockColumnRepository,
            mockTaskRepository
        )

        boardViewModel.uiState.value = boardViewModel.uiState.value.copy(board = board)

        // Act
        boardViewModel.onColumnCreation()

        // Assert
        assertEquals(0, boardViewModel.uiState.value.columns.size)

    }

    @Test
    fun createColumnSuccess() {
        // Arrange
        coEvery { mockColumnRepository.createColumn("columnName", board) } returns column

        boardViewModel = BoardViewModel(
            mockAuthService,
            mockLogService,
            mockBoardRepository,
            mockColumnRepository,
            mockTaskRepository
        )

        boardViewModel.uiState.value =
            boardViewModel.uiState.value.copy(newColumnName = "columnName")
        boardViewModel.uiState.value = boardViewModel.uiState.value.copy(board = board)

        // Act
        boardViewModel.onColumnCreation()

        // Assert
        assert(column == boardViewModel.uiState.value.columns[0])

    }

    @Test
    fun renameColumnEmptyName() {
        // Arrange
        val newColumn = Column(
            id = "newColumnId",
            name = "newColumnName",
            workspaceId = "workspaceId",
            boardId = "boardId"
        )

        coEvery { mockColumnRepository.renameColumn(column, "newColumnName") } returns newColumn
        boardViewModel = BoardViewModel(
            mockAuthService,
            mockLogService,
            mockBoardRepository,
            mockColumnRepository,
            mockTaskRepository
        )

        boardViewModel.uiState.value =
            boardViewModel.uiState.value.copy(columns = boardViewModel.uiState.value.columns + column)

        // Act
        boardViewModel.onRenamingColumn(column)

        // Assert
        assert(column == boardViewModel.uiState.value.columns[0])
    }

    @Test
    fun renameColumnSuccess() {
        // Arrange
        val newColumn = Column(
            id = "newColumnId",
            name = "newColumnName",
            workspaceId = "workspaceId",
            boardId = "boardId"
        )

        coEvery { mockColumnRepository.renameColumn(column, "newColumnName") } returns newColumn
        boardViewModel = BoardViewModel(
            mockAuthService,
            mockLogService,
            mockBoardRepository,
            mockColumnRepository,
            mockTaskRepository
        )

        boardViewModel.uiState.value =
            boardViewModel.uiState.value.copy(columns = boardViewModel.uiState.value.columns + column)
        boardViewModel.uiState.value =
            boardViewModel.uiState.value.copy(renamingColumnName = "newColumnName")

        // Act
        boardViewModel.onRenamingColumn(column)

        // Assert
        assert(newColumn == boardViewModel.uiState.value.columns[0])
    }

    @Test
    fun renameBoardEmptyName() {
        // Arrange
        val newBoard = Board(
            id = "newBoardId",
            name = "newBoardName",
            workspaceId = "workspaceId"
        )

        coEvery { mockBoardRepository.renameBoard(board, "newBoardName") } returns newBoard
        boardViewModel = BoardViewModel(
            mockAuthService,
            mockLogService,
            mockBoardRepository,
            mockColumnRepository,
            mockTaskRepository
        )

        boardViewModel.uiState.value = boardViewModel.uiState.value.copy(board = board)
        // Act
        boardViewModel.onRenaming()

        // Assert
        assert(board == boardViewModel.uiState.value.board)
    }

    @Test
    fun renameBoardSuccess() {
        // Arrange
        val newBoard = Board(
            id = "newBoardId",
            name = "newBoardName",
            workspaceId = "workspaceId"
        )

        coEvery { mockBoardRepository.renameBoard(board, "newBoardName") } returns newBoard
        boardViewModel = BoardViewModel(
            mockAuthService,
            mockLogService,
            mockBoardRepository,
            mockColumnRepository,
            mockTaskRepository
        )

        boardViewModel.uiState.value = boardViewModel.uiState.value.copy(board = board)
        boardViewModel.uiState.value =
            boardViewModel.uiState.value.copy(newBoardName = "newBoardName")

        // Act
        boardViewModel.onRenaming()

        // Assert
        assert(newBoard == boardViewModel.uiState.value.board)
    }

    @Test
    fun createTaskEmptyName() {
        // Arrange
        val task = Task(
            id = "taskId",
            name = "taskName",
            description = "taskDescription",
            workspaceId = "workspaceId",
            boardId = "boardId",
            columnId = "columnId"
        )
        coEvery { mockTaskRepository.createTask("taskName", column) } returns task

        boardViewModel = BoardViewModel(
            mockAuthService,
            mockLogService,
            mockBoardRepository,
            mockColumnRepository,
            mockTaskRepository
        )

        boardViewModel.uiState.value = boardViewModel.uiState.value.copy(board = board)
        boardViewModel.uiState.value =
            boardViewModel.uiState.value.copy(columns = boardViewModel.uiState.value.columns + column)
        boardViewModel.uiState.value =
            boardViewModel.uiState.value.copy(tasks = boardViewModel.uiState.value.tasks + (column to emptyList()))

        // Act
        boardViewModel.onTaskCreation(column)

        // Assert
        assertEquals(0, boardViewModel.uiState.value.tasks[column]!!.size)
    }

    @Test
    fun createTaskSuccess() {
        // Arrange
        val task = Task(
            id = "taskId",
            name = "taskName",
            description = "taskDescription",
            workspaceId = "workspaceId",
            boardId = "boardId",
            columnId = "columnId"
        )
        coEvery { mockTaskRepository.createTask("taskName", column) } returns task

        boardViewModel = BoardViewModel(
            mockAuthService,
            mockLogService,
            mockBoardRepository,
            mockColumnRepository,
            mockTaskRepository
        )

        boardViewModel.uiState.value = boardViewModel.uiState.value.copy(board = board)
        boardViewModel.uiState.value =
            boardViewModel.uiState.value.copy(columns = boardViewModel.uiState.value.columns + column)
        boardViewModel.uiState.value =
            boardViewModel.uiState.value.copy(tasks = boardViewModel.uiState.value.tasks + (column to emptyList()))
        boardViewModel.uiState.value = boardViewModel.uiState.value.copy(newTaskName = "taskName")

        // Act
        boardViewModel.onTaskCreation(column)

        // Assert
        assert(task == boardViewModel.uiState.value.tasks[column]!![0])

    }

    @Test
    fun deleteBoard() {
        // Arrange
        coEvery { mockBoardRepository.deleteBoard(board) } returns Unit

        boardViewModel = BoardViewModel(
            mockAuthService,
            mockLogService,
            mockBoardRepository,
            mockColumnRepository,
            mockTaskRepository
        )


        boardViewModel.uiState.value = boardViewModel.uiState.value.copy(board = board)

        // Act
        boardViewModel.onDelete { }

        // Assert
    }

}
