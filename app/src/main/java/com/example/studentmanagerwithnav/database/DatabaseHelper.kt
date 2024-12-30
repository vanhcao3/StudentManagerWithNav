package com.example.studentmanagerwithnav.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.studentmanagerwithnav.StudentModel

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "StudentDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_STUDENTS = "students"
        
        private const val KEY_DB_ID = "db_id"
        private const val KEY_NAME = "name"
        private const val KEY_STUDENT_ID = "student_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_STUDENTS (
                $KEY_DB_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_NAME TEXT,
                $KEY_STUDENT_ID TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
    }

    fun addStudent(student: StudentModel): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, student.name)
            put(KEY_STUDENT_ID, student.id)
        }
        val id = db.insert(TABLE_STUDENTS, null, values)
        db.close()
        return id
    }

    fun getAllStudents(): List<StudentModel> {
        val students = mutableListOf<StudentModel>()
        val selectQuery = "SELECT * FROM $TABLE_STUDENTS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val dbId = it.getInt(it.getColumnIndexOrThrow(KEY_DB_ID))
                    val name = it.getString(it.getColumnIndexOrThrow(KEY_NAME))
                    val studentId = it.getString(it.getColumnIndexOrThrow(KEY_STUDENT_ID))
                    students.add(StudentModel(name, studentId, dbId))
                } while (it.moveToNext())
            }
        }
        db.close()
        return students
    }

    fun updateStudent(student: StudentModel): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, student.name)
            put(KEY_STUDENT_ID, student.id)
        }
        return db.update(TABLE_STUDENTS, values, "$KEY_DB_ID = ?", 
            arrayOf(student.dbId.toString())).also { db.close() }
    }

    fun deleteStudent(dbId: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_STUDENTS, "$KEY_DB_ID = ?", 
            arrayOf(dbId.toString())).also { db.close() }
    }
} 