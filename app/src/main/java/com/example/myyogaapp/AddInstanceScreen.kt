package com.example.myyogaapp

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

// Function to show the DatePickerDialog
fun showDatePicker(context: android.content.Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            // Format the date as yyyy-MM-dd (e.g., 2023-11-06)
            val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            onDateSelected(selectedDate)
        },
        year,
        month,
        day
    )
    datePickerDialog.show()
}

@OptIn(ExperimentalMaterial3Api::class)
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

    // Get the current context for showing the DatePickerDialog
    val context = LocalContext.current

    // Gradient background matching other screens
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE1BEE7), Color(0xFFCE93D8))
    )

    Scaffold(
        modifier = Modifier.background(gradient),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Instance",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A00E0)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Date field with calendar picker
            OutlinedTextField(
                value = date,
                onValueChange = { }, // Empty since it's read-only
                label = { Text("Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                readOnly = true, // Prevents manual typing
                trailingIcon = {
                    IconButton(onClick = {
                        showDatePicker(context) { selectedDate ->
                            date = selectedDate
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = "Select Date",
                            tint = Color(0xFF4A00E0)
                        )
                    }
                }
            )
            // Hint to guide the user
            if (course != null) {
                Text(
                    text = "Please select a ${course.dayOfWeek}.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            OutlinedTextField(
                value = teacher,
                onValueChange = { teacher = it },
                label = { Text("Teacher") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            OutlinedTextField(
                value = comments,
                onValueChange = { comments = it },
                label = { Text("Comments (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (date.isEmpty() || teacher.isEmpty()) {
                        error = "Date and Teacher are required."
                    } else {
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        try {
                            val parsedDate = formatter.parse(date)
                            // Use Locale.ENGLISH for consistent day names
                            val dayOfWeek = SimpleDateFormat("EEEE", Locale.ENGLISH).format(parsedDate).uppercase()
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
                            error = "Something went wrong with the date."
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA))
            ) {
                Text("Add", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}