package com.example.kanban_android.domain.model

data class Column(
    val id: String = "",
    override val name: String = "",
    val workspaceId: String = "",
    val boardId: String = ""
) : NamedData
