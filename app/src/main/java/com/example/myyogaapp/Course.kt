package com.example.myyogaapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true) val courseId: Int = 0,
    val dayOfWeek: String,
    val time: String,
    val capacity: Int,
    val duration: Int,
    val price: Float,
    val type: String,
    val description: String,
    val level: String
)