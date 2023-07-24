package com.example.todo.presentation.add_edit_todo

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.database.Todo
import com.example.todo.data.repository.TodoRepository
import com.example.todo.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var todo by mutableStateOf<Todo?>(null)
        private set

    var completionTime: Calendar by mutableStateOf(Calendar.getInstance())
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var notificationEnabled by mutableStateOf(false)
        private set

    var category by mutableStateOf("")
        private set

    var attachments by mutableStateOf<Uri?>(null)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val todoId = savedStateHandle.get<Int>("todoId")!!
        if (todoId != -1) {
            viewModelScope.launch {
                todoRepository.getTodoById(todoId).let { todo ->
                    todo?.let {
                        title = todo.title
                        description = todo.description ?: ""
                        notificationEnabled = todo.notificationEnabled
                        category = todo.category
                        completionTime.time = todo.completionTime
                        attachments = Uri.parse(todo.attachments)
                        this@AddEditTodoViewModel.todo = todo
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditTodoEvent) {
        when (event) {
            is AddEditTodoEvent.OnCategoryChange -> category = event.category
            is AddEditTodoEvent.OnDescriptionChange -> description = event.description
            is AddEditTodoEvent.OnTitleChange -> title = event.title
            is AddEditTodoEvent.OnNotificationEnabledChange -> notificationEnabled =
                event.notificationChange

            is AddEditTodoEvent.OnAttachmentsChange -> {
                attachments = event.attachmentUri
            }

            is AddEditTodoEvent.OnTimeConfirm -> {
                completionTime.set(Calendar.HOUR_OF_DAY, event.hour)
                completionTime.set(Calendar.MINUTE, event.minute)
            }

            is AddEditTodoEvent.OnDateConfirm -> {
                val newDate = event.timestamp?.let { Date(it) } ?: Date()
                completionTime.time = newDate
            }

            is AddEditTodoEvent.OnSaveTodosClick -> {
                viewModelScope.launch {
                    if (title.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Title can not be empty"
                            )
                        )
                        return@launch
                    }

                    todoRepository.insertTodo(
                        Todo(
                            id = todo?.id,
                            title = title,
                            description = description,
                            creationTime = todo?.creationTime ?: Date(),
                            completionTime = completionTime.time,
                            isDone = todo?.isDone ?: false,
                            notificationEnabled = notificationEnabled,
                            category = category,
                            attachments = attachments.toString()
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}