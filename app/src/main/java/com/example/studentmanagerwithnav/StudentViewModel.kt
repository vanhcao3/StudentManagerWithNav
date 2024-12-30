package com.example.studentmanagerwithnav

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StudentRepository
    val allStudents: LiveData<List<StudentModel>>

    init {
        val dao = StudentDatabase.getDatabase(application).studentDao()
        repository = StudentRepository(dao)
        allStudents = repository.allStudents
    }

    fun insert(student: StudentModel) = viewModelScope.launch {
        repository.insert(student)
    }

    fun update(student: StudentModel) = viewModelScope.launch {
        repository.update(student)
    }

    fun delete(student: StudentModel) = viewModelScope.launch {
        repository.delete(student)
    }
} 