package com.example.myyogaapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses")
    fun getAllCourses(): LiveData<List<Course>>

    @Query("SELECT * FROM courses WHERE courseId = :id")
    fun getCourseById(id: Int): Course

    @Insert
    suspend fun insertCourse(course: Course)

    @Update
    suspend fun updateCourse(course: Course)

    @Delete
    suspend fun deleteCourse(course: Course)
}