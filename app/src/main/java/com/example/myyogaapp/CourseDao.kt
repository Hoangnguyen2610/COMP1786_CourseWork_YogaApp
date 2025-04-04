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

    @Query("""
        SELECT c.*, 
               (SELECT i.date FROM instances i WHERE i.courseId = c.courseId AND i.date >= :currentDateStr ORDER BY i.date LIMIT 1) AS nextDate,
               (SELECT i.teacher FROM instances i WHERE i.courseId = c.courseId AND i.date >= :currentDateStr ORDER BY i.date LIMIT 1) AS nextTeacher
        FROM courses c
    """)
    fun getCoursesWithNextInstance(currentDateStr: String): LiveData<List<CourseWithInstance>>

    @Query("SELECT * FROM courses")
    suspend fun getAllCoursesSync(): List<Course>

    @Query("DELETE FROM courses")
    suspend fun deleteAllCourses()

    // Added for search functionality
    @Query("""
        SELECT * FROM courses 
        WHERE LOWER(type) LIKE LOWER(:query) 
        OR LOWER(level) LIKE LOWER(:query) 
        OR LOWER(dayOfWeek) LIKE LOWER(:query)
    """)
    fun searchCourses(query: String): LiveData<List<Course>>
}