// Instance.kt
package com.example.myyogaapp

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "instances",
    foreignKeys = [ForeignKey(
        entity = Course::class,
        parentColumns = ["courseId"],
        childColumns = ["courseId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["courseId"])] // Add index here
)
data class Instance(
    @PrimaryKey(autoGenerate = true) val instanceId: Int = 0,
    val courseId: Int,
    val date: String,
    val teacher: String,
    val comments: String?
)