package com.example.myyogaapp

import androidx.room.Embedded

data class CourseWithInstance(
    @Embedded val course: Course,
    val nextDate: String?,
    val nextTeacher: String?
)