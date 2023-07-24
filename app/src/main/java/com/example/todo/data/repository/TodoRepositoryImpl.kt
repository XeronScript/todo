package com.example.todo.data.repository

import com.example.todo.data.database.Todo
import com.example.todo.data.database.TodoDao
import kotlinx.coroutines.flow.Flow

class TodoRepositoryImpl(
    private val dao: TodoDao
) : TodoRepository {

    override suspend fun insertTodo(todo: Todo) {
        dao.insertTodo(todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        dao.deleteTodo(todo)
    }

    override suspend fun getTodoById(id: Int): Todo? {
        return dao.getTodoById(id)
    }

    override fun getTodosByCategory(category: String): Flow<List<Todo>> {
        return dao.getTodosByCategory(category)
    }

    override fun getTodos(): Flow<List<Todo>> {
        return dao.getTodos()
    }
}