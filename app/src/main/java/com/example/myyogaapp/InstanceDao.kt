package com.example.myyogaapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface InstanceDao {
    @Query("SELECT * FROM instances WHERE courseId = :courseId")
    fun getInstancesForCourse(courseId: Int): LiveData<List<Instance>>

    @Insert
    suspend fun insertInstance(instance: Instance)

    @Query("SELECT * FROM instances WHERE teacher LIKE :query")
    fun searchByTeacher(query: String): LiveData<List<Instance>>

    @Query("SELECT * FROM instances")
    suspend fun getAllInstancesSync(): List<Instance>
}