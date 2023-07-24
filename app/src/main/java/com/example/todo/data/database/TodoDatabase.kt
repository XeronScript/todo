package com.example.todo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Todo::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {

    abstract val dao: TodoDao
}