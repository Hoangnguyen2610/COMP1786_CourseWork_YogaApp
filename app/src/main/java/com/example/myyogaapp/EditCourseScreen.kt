package com.example.myyogaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCourseScreen(navController: NavController, course: Course, db: AppDatabase) {
    var dayOfWeek by remember { mutableStateOf(course.dayOfWeek) }
    var time by remember { mutableStateOf(course.time) }
    var capacity by remember { mutableStateOf(course.capacity.toString()) }
    var duration by remember { mutableStateOf(course.duration.toString()) }
    var id by remember { mutableStateOf(course.courseId )}
    var price by remember { mutableStateOf(course.price.toString()) }
    var type by remember { mutableStateOf(course.type) }
    var description by remember { mutableStateOf(course.description ?: "") }
    var level by remember { mutableStateOf(course.level ?: "") }
    var error by remember { mutableStateOf("") }

    // Dropdown for Type
    var expanded by remember { mutableStateOf(false) }
    val typeOptions = listOf("Flow", "Aerial", "Family")

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE1BEE7), Color(0xFFCE93D8))
    )

    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.background(gradient)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Edit Course",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A00E0),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = dayOfWeek,
                onValueChange = { dayOfWeek = it },
                label = { Text("Day of Week") },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            )
            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Time (e.g., 10:00)") },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            )
            OutlinedTextField(
                value = capacity,
                onValueChange = { capacity = it },
                label = { Text("Capacity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            )
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (minutes)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            )
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price (£)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = type,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor().clip(RoundedCornerShape(12.dp))
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    typeOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                type = option
                                expanded = false
                            }
                        )
                    }
                }
            }
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)") },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            )
            OutlinedTextField(
                value = level,
                onValueChange = { level = it },
                label = { Text("Level (optional)") },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            )

            if (error.isNotEmpty()) {
                Text(text = error, color = Color.Red, fontSize = 14.sp)
            }

            Button(
                onClick = {
                    if (dayOfWeek.isEmpty() || time.isEmpty() || capacity.isEmpty() || duration.isEmpty() || price.isEmpty() || type.isEmpty()) {
                        error = "All required fields must be filled."
                    } else {
                        val capacityInt = capacity.toIntOrNull()
                        val durationInt = duration.toIntOrNull()
                        val priceFloat = price.toFloatOrNull()

                        if (capacityInt == null || durationInt == null || priceFloat == null) {
                            error = "Capacity, Duration, and Price must be valid numbers."
                        } else {
                            val updatedCourse = course.copy(
                                dayOfWeek = dayOfWeek,
                                time = time,
                                capacity = capacityInt,
                                duration = durationInt,
                                price = priceFloat,
                                type = type,
                                description = if (description.isBlank()) "" else description,
                                level = if (level.isBlank()) "" else level
                            )
                            scope.launch {
                                db.courseDao().updateCourse(updatedCourse)
                                navController.popBackStack("courseDetail/${course.courseId}", false)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA))
            ) {
                Text("Save Changes", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}