

// SearchScreen.kt
package com.example.myyogaapp

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Column

import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun SearchScreen(db: AppDatabase) {
    val instanceDao = db.instanceDao()
    var query by remember { mutableStateOf("") }
    val results by instanceDao.searchByTeacher("$query%").observeAsState(emptyList())
    Column {
        TextField(value = query, onValueChange = { query = it }, label = { Text("Search by Teacher") })
        LazyColumn {
            items(results) { instance ->
                Text("${instance.date} - ${instance.teacher}")
            }
        }
    }
}

// Add to YogaApp in MainActivity.kt
