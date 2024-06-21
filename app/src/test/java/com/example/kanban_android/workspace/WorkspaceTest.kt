package com.example.kanban_android.workspace

import com.example.kanban_android.MainDispatcherRule
import com.example.kanban_android.domain.model.Board
import com.example.kanban_android.domain.model.Column
import com.example.kanban_android.domain.model.User
import com.example.kanban_android.domain.model.Workspace
import com.example.kanban_android.domain.repositories.base.BoardRepository
import com.example.kanban_android.domain.repositories.base.ColumnRepository
import com.example.kanban_android.domain.repositories.base.TaskRepository
import com.example.kanban_android.domain.repositories.base.WorkspaceRepository
import com.example.kanban_android.domain.services.base.AuthService
import com.example.kanban_android.domain.services.base.LogService
import com.example.kanban_android.presentation.board.BoardViewModel
import com.example.kanban_android.presentation.home.HomeViewModel
import com.example.kanban_android.presentation.task.TaskViewModel
import com.example.kanban_android.presentation.workspace.WorkspaceViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock

class WorkspaceTest {
    @get:Rule
    val composeTestRule = MainDispatcherRule()

    private var mockAuthService = mockk<AuthService>()
    private var mockLogService = mockk<LogService>()
    private var mockBoardRepository = mockk<BoardRepository>()
    private var mockColumnRepository = mockk<ColumnRepository>()
    private var mockWorkspaceRepository = mockk<WorkspaceRepository>()
    private lateinit var workspaceViewModel: WorkspaceViewModel
    private lateinit var homeViewModel: HomeViewModel

    private var workspace = Workspace(
        id = "workspaceId",
        name = "workspaceName"
    )

    private var user = User(
        email = "email",
        uid = "uid"
    )

    private var board: Board = Board(
        id = "boardId",
        name = "boardName",
        workspaceId = "workspaceId"
    )

    @Test
    fun createWorkspaceSuccess() {
        // Arrange
        coEvery { mockWorkspaceRepository.createWorkspace("workspaceName", user) } returns workspace

        homeViewModel = HomeViewModel(
            mockAuthService,
            mockLogService,
            mockWorkspaceRepository
        )

        homeViewModel.uiState.value =
            homeViewModel.uiState.value.copy(newWorkspaceName = "workspaceName")
        homeViewModel.uiState.value = homeViewModel.uiState.value.copy(workspaces = emptyList())
        homeViewModel.uiState.value = homeViewModel.uiState.value.copy(user = user)

        // Act
        homeViewModel.onWorkspaceCreation { }

        // Assert
        assert(workspace == homeViewModel.uiState.value.workspaces[0])

    }

    @Test
    fun createWorkspaceEmptyName() {
        // Arrange
        coEvery { mockWorkspaceRepository.createWorkspace("workspaceName", user) } returns workspace

        homeViewModel = HomeViewModel(
            mockAuthService,
            mockLogService,
            mockWorkspaceRepository
        )

        homeViewModel.uiState.value = homeViewModel.uiState.value.copy(workspaces = emptyList())
        homeViewModel.uiState.value = homeViewModel.uiState.value.copy(user = user)

        // Act
        homeViewModel.onWorkspaceCreation { }

        // Assert
        assertEquals(0, homeViewModel.uiState.value.workspaces.size)
    }

    @Test
    fun deleteWorkspace() {
        // Arrange
        coEvery { mockWorkspaceRepository.deleteWorkspace(workspace) } returns Unit

        workspaceViewModel = WorkspaceViewModel(
            mockAuthService,
            mockLogService,
            mockWorkspaceRepository,
            mockBoardRepository,
            mockColumnRepository
        )


        workspaceViewModel.uiState.value =
            workspaceViewModel.uiState.value.copy(workspace = workspace)

        // Act
        workspaceViewModel.onDelete { }

        // Assert
    }

    @Test
    fun createBoardSuccess() {
        // Arrange
        coEvery { mockBoardRepository.createBoard("boardName", workspace) } returns board
        coEvery { mockColumnRepository.createColumn("TODO", board) } returns Column()
        coEvery { mockColumnRepository.createColumn("In Progress", board) } returns Column()
        coEvery { mockColumnRepository.createColumn("Done", board) } returns Column()

        workspaceViewModel = WorkspaceViewModel(
            mockAuthService,
            mockLogService,
            mockWorkspaceRepository,
            mockBoardRepository,
            mockColumnRepository
        )

        workspaceViewModel.uiState.value =
            workspaceViewModel.uiState.value.copy(workspace = workspace)
        workspaceViewModel.uiState.value =
            workspaceViewModel.uiState.value.copy(newBoardName = "boardName")
        workspaceViewModel.uiState.value =
            workspaceViewModel.uiState.value.copy(boards = emptyList())

        // Act
        workspaceViewModel.onBoardCreation { }

        // Assert
        assert(board == workspaceViewModel.uiState.value.boards[0])
    }

    @Test
    fun createBoardEmptyName() {
        // Arrange

        workspaceViewModel = WorkspaceViewModel(
            mockAuthService,
            mockLogService,
            mockWorkspaceRepository,
            mockBoardRepository,
            mockColumnRepository
        )
        workspaceViewModel.uiState.value =
            workspaceViewModel.uiState.value.copy(boards = emptyList())

        // Act
        workspaceViewModel.onBoardCreation { }

        // Assert
        assertEquals(0, workspaceViewModel.uiState.value.boards.size)
    }


}