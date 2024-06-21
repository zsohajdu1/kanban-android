package com.example.kanban_android.common.ext

import android.util.Patterns
import java.util.regex.Pattern

private const val PASS_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}\$"

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return this.isNotBlank() && Pattern.compile(PASS_PATTERN).matcher(this).matches()
}