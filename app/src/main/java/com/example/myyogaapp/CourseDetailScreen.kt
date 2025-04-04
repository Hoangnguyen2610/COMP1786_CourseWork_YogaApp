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

@OptIn(ExperimentalMaterial3Api::class) // Add this annotation to opt-in to experimental APIs
@Composable
fun CourseDetailScreen(
    navController: NavController,
    course: Course,
    db: AppDatabase
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE1BEE7), Color(0xFFCE93D8))
    )

    Scaffold(
        modifier = Modifier.background(gradient),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Course Details",
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
                    Text("Capacity: ${course.capacity}", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                    Text("Duration: ${course.duration} min", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                    Text("Price: £${course.price}", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                    Text("Description: ${course.description.takeIf { it.isNotEmpty() } ?: "None"}", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                    Text("Level: ${course.level.takeIf { it.isNotEmpty() } ?: "Not specified"}", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate("editCourse/${course.courseId}") },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA))
                ) {
                    Text("Edit Course", fontSize = 18.sp, color = Color.White)
                }
                Button(
                    onClick = { navController.navigate("addInstance/${course.courseId}") },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A00E0))
                ) {
                    Text("Add Instance", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}