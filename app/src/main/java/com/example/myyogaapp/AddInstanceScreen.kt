package com.example.myyogaapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AddInstanceScreen(courseId: Int, navController: NavController, db: AppDatabase) {
    val courseDao = db.courseDao()
    val instanceDao = db.instanceDao()
    val viewModel = AddInstanceViewModel(instanceDao)
    val courses by courseDao.getAllCourses().observeAsState(emptyList())
    val course = courses.find { it.courseId == courseId }
    var date by remember { mutableStateOf("") }
    var teacher by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        TextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (yyyy-MM-dd)") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = teacher,
            onValueChange = { teacher = it },
            label = { Text("Teacher") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = comments,
            onValueChange = { comments = it },
            label = { Text("Comments (optional)") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (date.isEmpty() || teacher.isEmpty()) {
                error = "Date and Teacher are required."
            } else {
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                try {
                    val parsedDate = formatter.parse(date)
                    val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(parsedDate).uppercase()
                    if (course?.dayOfWeek?.uppercase() != dayOfWeek) {
                        error = "Date must match course day: ${course?.dayOfWeek}"
                    } else {
                        val instance = Instance(
                            courseId = courseId,
                            date = date,
                            teacher = teacher,
                            comments = comments
                        )
                        viewModel.insertInstance(instance)
                        navController.popBackStack()
                    }
                } catch (e: Exception) {
                    error = "Invalid date format. Use yyyy-MM-dd."
                }
            }
        }) {
            Text("Add")
        }
    }
}