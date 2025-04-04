// SyncScreen.kt
package com.example.myyogaapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SyncScreen(db: AppDatabase, context: Context) {
    val courses by db.courseDao().getAllCourses().observeAsState(emptyList())

    Button(
        onClick = {
            if (isNetworkAvailable(context)) {
                val firestore = FirebaseFirestore.getInstance()
                courses.forEach { course ->
                    firestore.collection("courses")
                        .document("course_${course.courseId}")
                        .set(course)
                        .addOnSuccessListener { /* Handle success if needed */ }
                        .addOnFailureListener { /* Handle failure if needed */ }
                }
            }
        },
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Sync to Cloud")
    }
}

