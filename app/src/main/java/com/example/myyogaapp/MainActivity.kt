package com.example.myyogaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myyogaapp.ui.theme.MyYogaAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(this)

        setContent {
            MyYogaAppTheme {
                MyApp(db = db)
            }
        }
    }
}

@Composable
fun MyApp(db: AppDatabase) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController = navController, db = db)
        }
        composable(
            route = "addCourse?dayOfWeek={dayOfWeek}&time={time}&capacity={capacity}&duration={duration}&price={price}&type={type}&description={description}&level={level}",
            arguments = listOf(
                navArgument("dayOfWeek") { type = NavType.StringType; defaultValue = "" },
                navArgument("time") { type = NavType.StringType; defaultValue = "" },
                navArgument("capacity") { type = NavType.StringType; defaultValue = "0" },
                navArgument("duration") { type = NavType.StringType; defaultValue = "0" },
                navArgument("price") { type = NavType.StringType; defaultValue = "0" },
                navArgument("type") { type = NavType.StringType; defaultValue = "" },
                navArgument("description") { type = NavType.StringType; defaultValue = "" },
                navArgument("level") { type = NavType.StringType; defaultValue = "" }
            )
        ) {
            AddCourseScreen(navController = navController, db = db)
        }
        composable(
            route = "previewCourse/{dayOfWeek}/{time}/{capacity}/{duration}/{price}/{type}/{description}/{level}",
            arguments = listOf(
                navArgument("dayOfWeek") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType },
                navArgument("capacity") { type = NavType.IntType },
                navArgument("duration") { type = NavType.IntType },
                navArgument("price") { type = NavType.FloatType },
                navArgument("type") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("level") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val dayOfWeek = backStackEntry.arguments?.getString("dayOfWeek") ?: ""
            val time = backStackEntry.arguments?.getString("time") ?: ""
            val capacity = backStackEntry.arguments?.getInt("capacity") ?: 0
            val duration = backStackEntry.arguments?.getInt("duration") ?: 0
            val price = backStackEntry.arguments?.getFloat("price") ?: 0f // Fixed typo: "即将" to "price"
            val type = backStackEntry.arguments?.getString("type") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            val level = backStackEntry.arguments?.getString("level") ?: ""

            val course = Course(
                courseId = 0,
                dayOfWeek = dayOfWeek,
                time = time,
                capacity = capacity,
                duration = duration,
                price = price,
                type = type,
                description = description,
                level = level
            )

            PreviewCourseScreen(
                navController = navController,
                course = course,
                db = db
            )
        }
        composable(
            route = "courseDetail/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0
            var course by remember { mutableStateOf<Course?>(null) }
            var isLoading by remember { mutableStateOf(true) }

            LaunchedEffect(courseId) {
                withContext(Dispatchers.IO) {
                    val fetchedCourse = db.courseDao().getCourseById(courseId)
                    withContext(Dispatchers.Main) {
                        course = fetchedCourse
                        isLoading = false
                    }
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (course != null) {
                CourseDetailScreen(
                    navController = navController,
                    course = course!!,
                    db = db
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Course not found", color = Color.Red)
                }
            }
        }
        composable(
            route = "editCourse/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0
            var course by remember { mutableStateOf<Course?>(null) }
            var isLoading by remember { mutableStateOf(true) }

            LaunchedEffect(courseId) {
                withContext(Dispatchers.IO) {
                    val fetchedCourse = db.courseDao().getCourseById(courseId)
                    withContext(Dispatchers.Main) {
                        course = fetchedCourse
                        isLoading = false
                    }
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (course != null) {
                EditCourseScreen(
                    navController = navController,
                    course = course!!,
                    db = db
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Course not found", color = Color.Red)
                }
            }
        }
        composable(
            route = "addInstance/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0
            AddInstanceScreen(courseId = courseId, navController = navController, db = db)
        }
    }
}