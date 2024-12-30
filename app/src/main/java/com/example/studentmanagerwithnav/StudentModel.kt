package com.example.studentmanagerwithnav

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "students")
data class StudentModel(
    val name: String,
    @PrimaryKey
    val id: String,
    val dbId: Int = 0
) : Parcelable {
    override fun toString(): String {
        return "$name ($id)"
    }
}
