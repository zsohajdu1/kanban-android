package com.example.kanban_android.domain.model

data class Task(
    val id: String = "",
    override val name: String = "",
    val description: String = "",
    val workspaceId: String = "",
    val boardId: String = "",
    val columnId: String = ""
) : NamedData