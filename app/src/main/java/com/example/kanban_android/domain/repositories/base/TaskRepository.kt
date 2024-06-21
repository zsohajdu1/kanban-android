package com.example.kanban_android.domain.repositories.base

import com.example.kanban_android.domain.model.Column
import com.example.kanban_android.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasksOfColumn(column: Column): Flow<List<Task>>
    suspend fun createTask(name: String, column: Column): Task
    suspend fun deleteTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun renameTask(task: Task, newName: String): Task
    suspend fun renameDescription(task: Task, newDescription: String): Task
    suspend fun moveTask(task: Task, newColumn: Column): Task
}