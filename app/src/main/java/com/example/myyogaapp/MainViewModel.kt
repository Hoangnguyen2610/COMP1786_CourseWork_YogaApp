package com.example.myyogaapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val courseDao: CourseDao) : ViewModel() {
    val courses: LiveData<List<Course>> = courseDao.getAllCourses()

    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            courseDao.deleteCourse(course)
        }
    }
}