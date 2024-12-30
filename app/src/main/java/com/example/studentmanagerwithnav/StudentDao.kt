package com.example.studentmanagerwithnav

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StudentDao {
    @Query("SELECT * FROM students")
    fun getAllStudents(): LiveData<List<StudentModel>>

    @Insert
    suspend fun insertStudent(student: StudentModel)

    @Update
    suspend fun updateStudent(student: StudentModel)

    @Delete
    suspend fun deleteStudent(student: StudentModel)
} 