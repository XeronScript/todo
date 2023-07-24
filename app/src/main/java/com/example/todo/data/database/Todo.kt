package com.example.todo.data.database

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "creation_time")
    val creationTime: Date,

    @ColumnInfo(name = "completion_time")
    val completionTime: Date,

    @ColumnInfo(name = "is_done")
    val isDone: Boolean,

    @ColumnInfo(name = "notification_enabled")
    val notificationEnabled: Boolean,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "attachments")
    val attachments: String?
)
