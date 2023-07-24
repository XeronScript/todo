package com.example.todo.presentation.add_edit_todo

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.util.Date

sealed class AddEditTodoEvent {
    data class OnTitleChange(val title: String): AddEditTodoEvent()
    data class OnDescriptionChange(val description: String): AddEditTodoEvent()
    data class OnNotificationEnabledChange(val notificationChange: Boolean): AddEditTodoEvent()
    data class OnCategoryChange(val category: String): AddEditTodoEvent()
    data class OnAttachmentsChange(val attachmentUri: Uri?): AddEditTodoEvent()
    data class OnDateConfirm(val timestamp: Long?): AddEditTodoEvent()
    data class OnTimeConfirm(val hour: Int, val minute: Int): AddEditTodoEvent()
    object OnSaveTodosClick: AddEditTodoEvent()
}