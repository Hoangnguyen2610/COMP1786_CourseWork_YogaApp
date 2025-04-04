// AddCourseViewModel.kt
package com.example.myyogaapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AddCourseViewModel(private val courseDao: CourseDao) : ViewModel() {
    fun insertCourse(course: Course) {
        viewModelScope.launch {
            courseDao.insertCourse(course)
        }
    }
}