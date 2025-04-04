package com.example.myyogaapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MainScreen(navController: NavController, db: AppDatabase) {
    val currentDateStr = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }
    val coursesWithNextInstance by db.courseDao().getCoursesWithNextInstance(currentDateStr).observeAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addCourse") },
                contentColor = Color.White,
                containerColor = Color(0xFF8E24AA) // Purple color to match theme
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Course")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "My Yoga Courses",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A00E0), // Purple color to match theme
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (coursesWithNextInstance.isEmpty()) {
                    item {
                        Text(
                            text = "No courses available. Add a course to get started!",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            color = Color.Gray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(coursesWithNextInstance) { courseWithInstance ->
                        val course = courseWithInstance.course
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { navController.navigate("courseDetail/${course.courseId}") },
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "${course.type}${course.level?.takeIf { it.isNotEmpty() }?.let { " - $it" } ?: ""}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4A00E0) // Purple for emphasis
                                    )
                                    Text(
                                        text = "Every ${course.dayOfWeek} at ${course.time}",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                    if (courseWithInstance.nextDate != null && courseWithInstance.nextTeacher != null) {
                                        Text(
                                            text = "Next: ${courseWithInstance.nextDate} with ${courseWithInstance.nextTeacher}",
                                            fontSize = 12.sp,
                                            color = Color.DarkGray
                                        )
                                    } else {
                                        Text(
                                            text = "No upcoming instances",
                                            fontSize = 12.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            db.courseDao().deleteCourse(course)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Course",
                                        tint = Color(0xFFE91E63) // Pinkish-red color
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}