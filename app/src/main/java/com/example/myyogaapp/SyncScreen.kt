package com.example.myyogaapp

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.runBlocking

@Composable
fun SyncScreen(db: AppDatabase, context: Context) {
    val courses by db.courseDao().getAllCourses().observeAsState(emptyList())
    val instances by db.instanceDao().getInstancesForCourse(0).observeAsState(emptyList())

    Button(
        onClick = {
            if (isNetworkAvailable(context)) {
                val firestore = FirebaseFirestore.getInstance()
                runBlocking {
                    // Sync Courses
                    val allCourses = db.courseDao().getAllCoursesSync()
                    allCourses.forEach { course ->
                        firestore.collection("courses")
                            .document("course_${course.courseId}")
                            .set(course)
                            .addOnSuccessListener {  }
                            .addOnFailureListener {  }
                    }
                    // Sync Instances
                    val allInstances = db.instanceDao().getAllInstancesSync()
                    allInstances.forEach { instance ->
                        firestore.collection("instances")
                            .document("instance_${instance.instanceId}")
                            .set(instance)
                            .addOnSuccessListener { }
                            .addOnFailureListener { }
                    }
                }
            }
        },
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Sync to Cloud")
    }
}