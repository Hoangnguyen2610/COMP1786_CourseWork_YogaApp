package com.example.myyogaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class) // Add this to opt-in to experimental Material3 APIs
@Composable
fun PreviewCourseScreen(
    navController: NavController,
    course: Course,
    db: AppDatabase
) {
    val scope = rememberCoroutineScope()

    // Gradient background matching CourseDetailScreen
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE1BEE7), Color(0xFFCE93D8))
    )

    Scaffold(
        modifier = Modifier.background(gradient),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Preview Course",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A00E0) // Purple color matching CourseDetailScreen
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "${course.type} on ${course.dayOfWeek} at ${course.time}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Capacity: ${course.capacity}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = "Duration: ${course.duration} min",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = "Price: £${course.price}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = "Type: ${course.type}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = "Description: ${course.description.takeIf { it.isNotEmpty() } ?: "None"}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = "Level: ${course.level.takeIf { it.isNotEmpty() } ?: "Not specified"}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        // Pass the course details back to AddCourseScreen for editing
                        navController.navigate(
                            "addCourse?dayOfWeek=${course.dayOfWeek}&time=${course.time}&capacity=${course.capacity}&duration=${course.duration}&price=${course.price}&type=${course.type}&description=${course.description}&level=${course.level}&id=${course.courseId}"
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF4A00E0) // Purple color for the border and text
                    )
                ) {
                    Text("Edit", fontSize = 18.sp)
                }
                Button(
                    onClick = {
                        scope.launch {
                            db.courseDao().insertCourse(course)
                            navController.navigate("main") {
                                popUpTo("main") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA)) // Purple color matching CourseDetailScreen
                ) {
                    Text("Save", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}