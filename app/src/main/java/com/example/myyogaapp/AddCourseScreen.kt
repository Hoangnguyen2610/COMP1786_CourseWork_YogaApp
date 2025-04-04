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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseScreen(navController: NavController, db: AppDatabase) {
    val dayOfWeekParam = navController.currentBackStackEntry?.arguments?.getString("dayOfWeek") ?: ""
    val timeParam = navController.currentBackStackEntry?.arguments?.getString("time") ?: ""
    val capacityParam = navController.currentBackStackEntry?.arguments?.getString("capacity")?.toIntOrNull() ?: 0
    val durationParam = navController.currentBackStackEntry?.arguments?.getString("duration")?.toIntOrNull() ?: 0
    val priceParam = navController.currentBackStackEntry?.arguments?.getString("price")?.toFloatOrNull() ?: 0f
    val typeParam = navController.currentBackStackEntry?.arguments?.getString("type") ?: ""
    val descriptionParam = navController.currentBackStackEntry?.arguments?.getString("description") ?: ""
    val levelParam = navController.currentBackStackEntry?.arguments?.getString("level") ?: ""

    var dayOfWeek by remember { mutableStateOf(dayOfWeekParam) }
    var time by remember { mutableStateOf(timeParam) }
    var capacity by remember { mutableStateOf(if (capacityParam != 0) capacityParam.toString() else "") }
    var duration by remember { mutableStateOf(if (durationParam != 0) durationParam.toString() else "") }
    var price by remember { mutableStateOf(if (priceParam != 0f) priceParam.toString() else "") }
    var type by remember { mutableStateOf(typeParam) }
    var description by remember { mutableStateOf(descriptionParam) }
    var level by remember { mutableStateOf(levelParam) }
    var error by remember { mutableStateOf("") }

    var dayExpanded by remember { mutableStateOf(false) }
    var timeExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }

    val dayOptions = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val timeOptions = listOf(
        "08:00", "09:00", "10:00", "11:00", "12:00",
        "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00"
    )
    val typeOptions = listOf("Flow Yoga", "Aerial Yoga", "Family Yoga")

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE1BEE7), Color(0xFFCE93D8))
    )

    Scaffold(
        modifier = Modifier.background(gradient),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Course",
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
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = dayExpanded,
                onExpandedChange = { dayExpanded = !dayExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = dayOfWeek,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Day of Week") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dayExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .clip(RoundedCornerShape(12.dp))
                )
                ExposedDropdownMenu(
                    expanded = dayExpanded,
                    onDismissRequest = { dayExpanded = false }
                ) {
                    dayOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                dayOfWeek = option
                                dayExpanded = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = timeExpanded,
                onExpandedChange = { timeExpanded = !timeExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = time,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Time") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = timeExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .clip(RoundedCornerShape(12.dp))
                )
                ExposedDropdownMenu(
                    expanded = timeExpanded,
                    onDismissRequest = { timeExpanded = false }
                ) {
                    timeOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                time = option
                                timeExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = capacity,
                onValueChange = { capacity = it },
                label = { Text("Capacity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (minutes)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price (£)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = !typeExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = type,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .clip(RoundedCornerShape(12.dp))
                )
                ExposedDropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false }
                ) {
                    typeOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                type = option
                                typeExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            OutlinedTextField(
                value = level,
                onValueChange = { level = it },
                label = { Text("Level") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (dayOfWeek.isEmpty() || time.isEmpty() || capacity.isEmpty() ||
                        duration.isEmpty() || price.isEmpty() || type.isEmpty() || level.isEmpty()
                    ) {
                        error = "All required fields must be filled."
                    } else {
                        val capacityInt = capacity.toIntOrNull()
                        val durationInt = duration.toIntOrNull()
                        val priceFloat = price.toFloatOrNull()

                        if (capacityInt == null || durationInt == null || priceFloat == null) {
                            error = "Capacity, Duration, and Price must be valid numbers."
                        } else {
                            val descriptionParam = description.ifEmpty { "none" }
                            navController.navigate(
                                "previewCourse/$dayOfWeek/$time/$capacityInt/$durationInt/$priceFloat/$type/$descriptionParam/$level"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA))
            ) {
                Text("Preview Course", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}