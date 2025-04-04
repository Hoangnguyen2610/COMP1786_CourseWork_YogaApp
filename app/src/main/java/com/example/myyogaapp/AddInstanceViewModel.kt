package com.example.myyogaapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AddInstanceViewModel(private val instanceDao: InstanceDao) : ViewModel() {
    fun insertInstance(instance: Instance) {
        viewModelScope.launch {
            instanceDao.insertInstance(instance)
        }
    }
}