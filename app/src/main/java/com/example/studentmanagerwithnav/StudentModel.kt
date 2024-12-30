package com.example.studentmanagerwithnav

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentModel(
    val name: String,
    val id: String,
    val dbId: Int = 0
) : Parcelable {
    override fun toString(): String = "$name ($id)"
}