package com.example.studentmanagerwithnav

import androidx.lifecycle.LiveData

class StudentRepository(private val studentDao: StudentDao) {
    val allStudents: LiveData<List<StudentModel>> = studentDao.getAllStudents()

    suspend fun insert(student: StudentModel) {
        studentDao.insertStudent(student)
    }

    suspend fun update(student: StudentModel) {
        studentDao.updateStudent(student)
    }

    suspend fun delete(student: StudentModel) {
        studentDao.deleteStudent(student)
    }
} 